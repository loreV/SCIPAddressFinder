package org.bluejay.network.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration class for the discoverable server.
 */
@Component
public final class Configuration {


    /**
     * Constants in the application.
     */
    static final boolean FLAG_CONTINUE = true;
    /**
     * Flag to broadcast.
     */
    static final boolean IS_TO_BE_BROADCAST = true;
    /**
     * Initial host address.
     */
    static final String HOST_INITIAL_ADDRESS = "0.0.0.0";
    /**
     * Size of the buffer to write to in byte.
     */
    static final int BUFFER_BYTE_SIZE = 1500;

    /**
     * Specify the port.
     */
    private final int port;
    /**
     * Specify the Bluejay name to be discovered.
     */
    private final String name;
    /**
     * Specifies the message to match for the discovery (sort of a password).
     */
    private final String message;

    /**
     * Constructs the configuration.
     *
     * @param dronePort    port at which the script listens to.
     * @param droneName    name of the drone.
     * @param droneMessage message to match for the discovery (sort of a password).
     */
    public Configuration(@Value("${drone.port}") final int dronePort,
                         @Value("${drone.name:blue}") final String droneName,
                         @Value("${drone.message:BJ_LOOKUP}") final String droneMessage) {
        this.port = dronePort;
        this.name = droneName;
        this.message = droneMessage;
    }

    /**
     * Gets the port at which the server is listening.
     *
     * @return the port as an int.
     */
    int getPORT() {
        return port;
    }

    /**
     * Gets the name of the drone.
     *
     * @return the name of the drone.
     */
    String getName() {
        return name;
    }

    /**
     * Gets the message.
     *
     * @return Message
     */
    String getMessage() {
        return message;
    }
}
