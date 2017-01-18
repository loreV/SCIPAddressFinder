package org.bluejay.network.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class Application {


    @Autowired
    public Application(final DiscoveringUDPThread discoveringUDPThread) {
        Thread discoveryThread = new Thread(discoveringUDPThread.getInstance());
        discoveryThread.start();
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
