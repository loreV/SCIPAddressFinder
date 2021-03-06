package org.bluejay.network.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

/**
 * Client entry-point.
 */
@Configuration
@ComponentScan
public class Client {

    /**
     * Starts the client.
     *
     * @param discoveringThread thread for discoverying new pi instances.
     */
    @Autowired
    public Client(final DiscoveringThread discoveringThread) {

        final Scanner scanner = new Scanner(System.in);

        System.out.println("Hit a key or press [ENTER] "
                + "to scan the network for BlueJay(s)");

        String readLine = scanner.nextLine();


        while (readLine != null) {
            if (readLine.isEmpty()) {
                final Thread discoveryThread = new Thread(discoveringThread);
                discoveryThread.start();
            }
            if (scanner.hasNextLine()) {
                readLine = scanner.nextLine();
            } else {
                readLine = null;
            }
        }
    }

    /**
     * Entry point to the application.
     *
     * @param args starting string arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Client.class, args);
    }
}
