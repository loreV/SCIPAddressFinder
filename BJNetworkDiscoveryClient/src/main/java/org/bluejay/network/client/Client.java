package org.bluejay.network.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Client entry-point.
 */
@Configuration
@ComponentScan
@PropertySource("application.properties")
public class Client {

    @Autowired
    public Client(final DiscoveringThread discoveringThread) {
        Thread discoveryThread = new Thread(discoveringThread);
        discoveryThread.start();
    }

    public static void main(final String[] args) {
        SpringApplication.run(Client.class, args);
    }


}
