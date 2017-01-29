package org.bluejay.network.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ConfigurationTest {

    private final Configuration candidateConfiguration;

    public ConfigurationTest() {
        candidateConfiguration = new Configuration(8888, "TestMessage");
    }

    @Test
    public void getPort() {
        assertEquals(candidateConfiguration.getPort(), 8888);
    }

    @Test
    public void getBroadcastMessage() {
        assertEquals(candidateConfiguration.getBroadcastMessage(), "TestMessage");
    }

}