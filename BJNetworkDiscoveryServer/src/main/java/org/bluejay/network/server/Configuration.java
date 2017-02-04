package org.bluejay.network.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration class for the discoverable server.
 */
@Component
public class Configuration {


    /**
     * Constants in the application.
     */
    static final boolean FLAG_CONTINUE = true;
    static final boolean IS_TO_BE_BROADCAST = true;
    static final String HOST_INITIAL_ADDRESS = "0.0.0.0";
    static final int BUFFER_BYTE_SIZE = 15000;

    /**
     * Fields defined in the application.properties.
     */
    private final int port;
    private final String name;
    private final String message;

    public Configuration(@Value("${drone.port}") final int port,
                         @Value("${drone.name:blue}") final String name,
                         @Value("${drone.message:BJ_LOOKUP}") final String message) {
        this.port = port;
        this.name = name;
        this.message = message;
    }

    int getPORT() {
        return port;
    }

    String getName() {
        return name;
    }

    String getMessage() {
        return message;
    }
}
