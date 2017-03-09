package org.bluejay.network.client.outputter;

import org.apache.log4j.Logger;
import org.bluejay.network.client.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * File writer.
 */
@Component
public class FileOuputter implements IOutputter {
    /**
     * File path to write up the file.
     */
    private final String filePath;
    /**
     * Logger object used for logging.
     */
    private final Logger logger;


    /**
     * Constructor of the file outputter.
     *
     * @param conf configuration sent
     */
    public FileOuputter(final Configuration conf) {
        filePath = conf.getPathForOutput();
        logger = Logger.getLogger(this.getClass());
    }


    /**
     * Dumps the given lines in a file.
     *
     * @param lines corresponding to drones.
     */
    @Override
    public void output(final List<String> lines) {
        final String pathToFile = filePath + "/foundDevices.txt";
        final Path file = Paths.get(pathToFile);
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (final IOException e) {
            logger.warn(
                    String.format("An error was thrown while trying "
                                    + "to write the foundDevices "
                                    + "file in position %s",
                            pathToFile)
            );
        }
    }

}
