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
    /**
     * Flags whether this needs to be broadcasted.
     */
    static final boolean IS_TO_BE_BROADCAST = true;
    /**
     * Broadcasting address.
     */
    static final String BROADCAST_ADDRESS = "255.255.255.255";
    static final int BUFFER_BYTE_SIZE = 15000;


    private final String pathForOutput;
    /**
     * Field defined in the application.properties.
     */
    private final int port;
    /**
     * Field broadcast message used to broadcast and
     * get the answer from the server side.
     */
    private final String broadcastMessage;

    /**
     * Constructs the configuration with the required values.
     *
     * @param networkPort network port used for connection
     * @param message     message broadcasted and in the need for matching.
     */
    Configuration(@Value("${drone.port}") final int networkPort,
                  @Value("${drone.message}") final String message,
                  @Value("${drone.path}") final String path)

    {
        this.pathForOutput = path;
        this.port = networkPort;
        this.broadcastMessage = message;
    }

    /**
     * Get the port.
     *
     * @return network port in configuration.
     */
    int getPort() {
        return port;
    }

    /**
     * Get the message.
     *
     * @return string message to be matched on the server side.
     */
    String getBroadcastMessage() {
        return broadcastMessage;
    }

    public String getPathForOutput() {
        return pathForOutput;
    }
}
