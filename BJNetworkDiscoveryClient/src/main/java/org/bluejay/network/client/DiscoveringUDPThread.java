package org.bluejay.network.client;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.Normalizer;
import java.util.Enumeration;

/**
 * Discovering Thread making use of the UDP protocol to detect devices of interest on the same network.
 */
@Component
public class DiscoveringUDPThread implements Runnable {

    private final Configuration configuration;
    private final Logger logger;
    private DatagramSocket datagramSocket;

    @Autowired
    protected DiscoveringUDPThread(final Configuration configuration) {
        this.configuration = configuration;
        logger = Logger.getLogger(this.getClass());
    }

    public void run() {
        // Find the server using UDP broadcast
        try {
            initializeDatagramSocket();
            DatagramPacket datagramPacket = sendPackage();

            logger.info(String.format("%s >>> Request packet sent to: %s (DEFAULT)",
                    getClass().getName(),
                    Configuration.BROADCAST_ADDRESS));

            broadCastMessage(datagramPacket.getData());

            //Wait for a response
            final DatagramPacket receivePacket = getResponse();
            //We have a response
            final String dataReturned = normalizedReceivedData(receivePacket);

            logger.info(String.format("%s >>> Broadcast response from BlueJay: %s",
                    receivePacket.getAddress().getHostAddress(),
                    dataReturned));
            //Check if the message is correct
            final String message = new String(receivePacket.getData()).trim();

            if (message.equals(configuration.getBroadcastMessage())) {
                logger.info(receivePacket.getAddress());
            }
            //Close the port!
            datagramSocket.close();
        } catch (final IOException ex) {
            logger.error(ex.getMessage());
        }


    }

    /**
     * It Cleans the received data.
     *
     * @param receivePacket received datagram packet.
     * @return cleaned String.
     */
    private String normalizedReceivedData(final DatagramPacket receivePacket) {
        String dataReturned = Normalizer.normalize(
                new String(receivePacket.getData()),
                Normalizer.Form.NFKD
        );
        dataReturned = dataReturned.replaceAll("\\p{C}", "");
        return dataReturned;
    }

    /**
     * Receives the response back from the server.
     *
     * @return DatagramPacket as a response.
     * @throws IOException when input error occurs.
     */
    private DatagramPacket getResponse() throws IOException {
        byte[] receivedBuffer = new byte[Configuration.BUFFER_BYTE_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(receivedBuffer, receivedBuffer.length);
        datagramSocket.receive(receivePacket);
        return receivePacket;
    }

    /**
     * Broadcast the message over all the network interfaces.
     */
    private void broadCastMessage(final byte[] dataForBroadCast) throws IOException {

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue; // Don't want to broadcast to the loopback interface
            }

            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null) {
                    continue;
                }

                final DatagramPacket sendPacket = new DatagramPacket(
                        dataForBroadCast,
                        dataForBroadCast.length,
                        broadcast,
                        configuration.getPort());

                datagramSocket.send(sendPacket);

                logger.trace(
                        String.format("%s >>> Request packet sent to: %s; Interface: %s",
                                getClass().getName(),
                                broadcast.getHostAddress(),
                                networkInterface.getDisplayName()));
            }
        }
    }

    /**
     * Send the package to the initial broadcast address.
     * 'https://en.wikipedia.org/wiki/Broadcast_address'.
     *
     * @return DatagramPacket sent.
     * @throws IOException error connected with sending the package.
     */
    private DatagramPacket sendPackage() throws IOException {
        byte[] sendData = configuration.getBroadcastMessage().getBytes();

        DatagramPacket sendPacket = new DatagramPacket(
                sendData,
                sendData.length,
                InetAddress.getByName(Configuration.BROADCAST_ADDRESS),
                configuration.getPort());

        datagramSocket.send(sendPacket);
        return sendPacket;
    }

    /**
     * Initialize the datagram socket and sets the broadcast option.
     *
     * @throws SocketException happened upon opening a datagram socket.
     */
    private void initializeDatagramSocket() throws SocketException {
        datagramSocket = new DatagramSocket();
        //Open a random port to send the package
        datagramSocket.setBroadcast(true);
    }


}
