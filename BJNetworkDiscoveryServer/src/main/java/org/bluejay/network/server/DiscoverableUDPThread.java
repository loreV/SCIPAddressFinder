package org.bluejay.network.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;

@Component
public class DiscoverableUDPThread implements Runnable {

    @Value("org.bluejay.network.port")
    private static int PORT;

    @Value("org.bluejay.network.name")
    private static String NAME;

    @Value("org.bluejay.network.message")
    private static String MESSAGE;

    private static final boolean FLAG_CONTINUE = true;

    private static final boolean IS_TO_BE_BROADCAST = true;
    private static final String HOST_INITIAL_ADDRESS = "0.0.0.0";
    private static final int BUFFER_BYTE_SIZE = 15000;

    private static DiscoverableUDPThread discoverableUDPThreadInstance;

    private final Logger logger;
    private DatagramSocket datagramSocket;

    protected DiscoverableUDPThread() {
        logger = Logger.getLogger(this.getClass());
    }

    /**
     *  Main loop containing the logic for receiving packages.
     */
    public void run() {
        try {
            initializeDatagramSocket();
            while (FLAG_CONTINUE) {
                logger.info(getClass().getName() + " : Ready to receive broadcast packets!");
                final DatagramPacket packet = getDatagramPacket();
                //Packet received
                logger.info(getClass().getName() + " : Discovery packet received from: " + packet.getAddress().getHostAddress());
                logger.info(getClass().getName() + " : Packet received; data: " + new String(packet.getData()));

                if (verifyEqualityMessageReceived(packet)) {
                    final DatagramPacket sentDatagram = sendResponse(packet);
                    logger.info(getClass().getName() + ">>>Sent packet to: " + sentDatagram.getAddress().getHostAddress());
                }
            }
        } catch (SocketException ioException) {
            logger.error("An error happened while waiting to be discovered", ioException);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the package received message is the expected.
     *
     * @param packet
     * @return true if the message is found to be equal to the expected one.
     * @throws IOException
     */
    private boolean verifyEqualityMessageReceived(final DatagramPacket packet) throws IOException {
        final String message = new String(packet.getData()).trim();
        return message.equals(MESSAGE);
    }

    /**
     * Send response back to server containing the IP address of the current machine on the network.
     *
     * @param packet
     * @return
     * @throws IOException
     */
    private DatagramPacket sendResponse(DatagramPacket packet) throws IOException {
        byte[] sendData = (MESSAGE + ":" + NAME).getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
        datagramSocket.send(sendPacket);
        return sendPacket;
    }

    /**
     * Makes a datagram packet from a buffer a datagram packet and returns it.
     *
     * @return
     * @throws IOException
     */
    private DatagramPacket getDatagramPacket() throws IOException {
        //Receive a packet
        byte[] receivedBuffer = new byte[BUFFER_BYTE_SIZE];
        DatagramPacket packet = new DatagramPacket(receivedBuffer, receivedBuffer.length);
        datagramSocket.receive(packet);
        return packet;
    }


    /**
     * Initialize a datagram socket (port, broadcast and initial address)
     *
     * @throws SocketException
     * @throws UnknownHostException
     */
    private void initializeDatagramSocket() throws SocketException, UnknownHostException {
        //Keep a socket open to listen to all the UDP trafic that is destined for this port
        datagramSocket = new DatagramSocket(PORT, InetAddress.getByName(HOST_INITIAL_ADDRESS));
        datagramSocket.setBroadcast(IS_TO_BE_BROADCAST);
    }

    /**
     * Singleton instance of the discovering UDP thread.
     *
     * @return DiscoveringUDPThread singleton instance
     */
    public DiscoverableUDPThread getInstance() {
        if (discoverableUDPThreadInstance == null) {
            discoverableUDPThreadInstance = new DiscoverableUDPThread();
        }
        return discoverableUDPThreadInstance;
    }

}
