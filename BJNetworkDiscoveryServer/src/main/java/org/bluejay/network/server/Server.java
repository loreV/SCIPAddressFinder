package org.bluejay.network.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Server entry-point.
 */
@Configuration
@ComponentScan
@PropertySource("file:${user.home}/preferences/application.properties")
public class Server {


    @Autowired
    public Server(final DiscoverableThread discoverableUDPThread) {
        final Thread discoveryThread = new Thread(discoverableUDPThread);
        discoveryThread.start();
    }

    public static void main(final String[] args) {
        SpringApplication.run(Server.class, args);
    }

}
