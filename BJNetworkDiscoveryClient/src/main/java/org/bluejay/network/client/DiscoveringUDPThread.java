package org.bluejay.network.client;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

@Component
public class DiscoveringUDPThread implements Runnable {

    private static DiscoveringUDPThread discoveringUDPThreadInstance;

    private final Logger logger;
    private DatagramSocket datagramSocket;

    protected DiscoveringUDPThread() {
        logger = Logger.getLogger(this.getClass());
    }

    public void run() {
        // Find the server using UDP broadcast
        try {
            datagramSocket = new DatagramSocket();
            //Open a random port to send the package
            datagramSocket.setBroadcast(true);

            byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();

            //Try the 255.255.255.255 first
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 8888);
            datagramSocket.send(sendPacket);
            logger.info(getClass().getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");


            // Broadcast the message over all the network interfaces
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

                    // Send the broadcast package!
                    sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
                    datagramSocket.send(sendPacket);

                    logger.info(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                }
            }

            logger.info(getClass().getName() + ">>> Done looping over all network interfaces. Now waiting for a reply!");

            //Wait for a response
            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            datagramSocket.receive(receivePacket);

            //We have a response
            logger.info(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

            //Check if the message is correct
            final String message = new String(receivePacket.getData()).trim();
            if (message.equals("DISCOVER_FUIFSERVER_RESPONSE")) {
                //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                logger.info(receivePacket.getAddress());
            }

            //Close the port!
            datagramSocket.close();
        } catch (final IOException ex) {
            logger.error("An error occured while discovering BJ", ex);
        }


    }

    /**
     * Singleton instance of the discovering UDP thread.
     * @return DiscoveringUDPThread singleton instance
     */
    public DiscoveringUDPThread getInstance() {
        if(discoveringUDPThreadInstance == null ) {
            discoveringUDPThreadInstance = new DiscoveringUDPThread();
        }
        return discoveringUDPThreadInstance;
    }


}
