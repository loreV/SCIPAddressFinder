package org.bluejay.network.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Discoverable thread used to receive and inform the client about BlueJay presence in the network.
 */
@Component
public class DiscoverableThread implements Runnable {


    private final Configuration conf;
    private DatagramSocket datagramSocket;
    private final Logger logger;

    @Autowired
    public DiscoverableThread(final Configuration configuration) {
        logger = Logger.getLogger(this.getClass());
        conf = configuration;
    }

    /**
     * Main loop containing the logic for receiving packages.
     */
    public void run() {
        try {

            initializeDatagramSocket();

            while (Configuration.FLAG_CONTINUE) {
                logger.info(getClass().getName() + " : Ready to receive broadcast packets!");
                final DatagramPacket packet = getDatagramPacket();
                //Packet received
                logger.info(String.format("%s : Discovery packet received from: %s",
                        getClass().getName(),
                        packet.getAddress().getHostAddress()));

                logger.info(String.format("%s : Packet received; by: %s",
                        getClass().getName(),
                        packet.getAddress().getHostAddress()));

                if (verifyEqualityMessageReceived(packet)) {
                    sendDatagramPacket(packet);
                }
            }
        } catch (final SocketException ioException) {
            logger.error("An error happened while waiting to be discovered", ioException);
        } catch (final UnknownHostException unknownException) {
            unknownException.printStackTrace();
        } catch (final IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void sendDatagramPacket(final DatagramPacket packet) throws IOException {
        final DatagramPacket sentDatagram = sendResponse(packet);
        logger.info(
                String.format("%s >>>Sent packet to: %s",
                        getClass().getName(),
                        sentDatagram.getAddress().getHostAddress()
                )
        );
    }

    /**
     * Check if the package received message is the expected.
     *
     * @param packet received.
     * @return true if the message is found to be equal to the expected one.
     * @throws IOException exception thrown on input.
     */
    private boolean verifyEqualityMessageReceived(final DatagramPacket packet) throws IOException {
        final String message = new String(packet.getData()).trim();
        return message.equals(conf.getMessage());
    }

    /**
     * Send response back to server containing the IP address of the current machine on the network.
     *
     * @param packet to be sent
     * @return the sent package
     * @throws IOException exception thrown on output.
     */
    private DatagramPacket sendResponse(final DatagramPacket packet) throws IOException {
        byte[] sendData = conf.getName().getBytes();
        final DatagramPacket sendPacket = new DatagramPacket(
                sendData,
                sendData.length,
                packet.getAddress(),
                packet.getPort());
        datagramSocket.send(sendPacket);
        return sendPacket;
    }

    /**
     * Makes a datagram packet from a buffer a datagram packet and returns it.
     *
     * @return DatagramPacket received.
     * @throws IOException exception thrown on output.
     */
    private DatagramPacket getDatagramPacket() throws IOException {
        //Receive a packet
        final byte[] receivedBuffer = new byte[Configuration.BUFFER_BYTE_SIZE];
        final DatagramPacket packet = new DatagramPacket(receivedBuffer, receivedBuffer.length);
        datagramSocket.receive(packet);
        return packet;
    }


    /**
     * Initialize a datagram socket (port, broadcast and initial address).
     *
     * @throws SocketException      error when creating the socket.
     * @throws UnknownHostException error thrown when the host is not known
     */
    private DatagramSocket initializeDatagramSocket() throws SocketException, UnknownHostException {
        //Keep a socket open to listen to all the UDP traffic that is destined for this port
        datagramSocket = new DatagramSocket(conf.getPORT(), InetAddress.getByName(Configuration.HOST_INITIAL_ADDRESS));
        datagramSocket.setBroadcast(Configuration.IS_TO_BE_BROADCAST);
        return datagramSocket;
    }


}
