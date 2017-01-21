package org.bluejay.network.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Application entry-point.
 */
@Configuration
@ComponentScan
public class Application {


    @Autowired
    public Application(final DiscoverableUDPThread discoverableUDPThread) {
        final Thread discoveryThread = new Thread(discoverableUDPThread);
        discoveryThread.start();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
