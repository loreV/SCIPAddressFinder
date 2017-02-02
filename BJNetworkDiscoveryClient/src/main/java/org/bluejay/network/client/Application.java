package org.bluejay.network.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Application entry-point.
 */
@Configuration
@ComponentScan
public class Application {

    @Autowired
    public Application(final DiscoveringThread discoveringThread) {
        Thread discoveryThread = new Thread(discoveringThread);
        discoveryThread.start();
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
