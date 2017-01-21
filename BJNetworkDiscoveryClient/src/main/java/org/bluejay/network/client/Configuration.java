package org.bluejay.network.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration class loading in the data from properties if provided.
 */
@Component
public class Configuration {

    /**
     * Constants in the application.
     */
    static final boolean FLAG_CONTINUE = true;
    static final boolean IS_TO_BE_BROADCAST = true;
    static final String BROADCAST_ADDRESS = "255.255.255.255";
    public static final int BUFFER_BYTE_SIZE = 15000;

    /**
     * Fields defined in the application.properties.
     */
    private final int port;
    private final String message;

    public Configuration(@Value("${drone.port}") final int port,
                         @Value("${drone.message}") final String message) {
        this.port = port;
        this.message = message;
    }

    int getPort() {
        return port;
    }

    String getMessage() {
        return message;
    }
}