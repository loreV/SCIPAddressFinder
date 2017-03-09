package org.bluejay.network.client.outputter;

import java.util.List;

/**
 * Ouputter interface.
 */
public interface IOutputter {
    /**
     * Some kind of outputting implementation.
     *
     * @param lines includes the lines that should be written out.
     */
    void output(List<String> lines);
}
