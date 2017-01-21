package org.bluejay.network.exception;

/**
 * Generic BJ network discovery exception.
 * Can be easily caught by container applications.
 */
public class BJNetworkDiscoveryException extends Exception {

    public BJNetworkDiscoveryException(final String message) {
        super(message);
    }

    public BJNetworkDiscoveryException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
