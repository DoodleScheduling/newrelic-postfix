package com.newrelic.metrics.publish;

import com.newrelic.metrics.publish.configuration.ConfigurationException;
import org.junit.Test;

public class PostfixAgentTest {

    @Test
    public void testAgentCreation() throws ConfigurationException {
        PostfixAgent pa = new PostfixAgent("host1");
    }

    @Test
    public void testPollCycle() throws ConfigurationException {
        PostfixAgent pa = new PostfixAgent("host1");
        pa.pollCycle();
    }
}
