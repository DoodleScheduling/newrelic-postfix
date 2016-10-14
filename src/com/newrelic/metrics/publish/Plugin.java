package com.newrelic.metrics.publish;

import com.newrelic.metrics.publish.configuration.ConfigurationException;

public class Plugin {
    public static void main(String[] args) {
        try {
            Runner runner = new Runner();
            runner.add(new PostfixAgentFactory());
            runner.setupAndRun(); // Never returns
        } catch (ConfigurationException e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(-1);
        }
    }
}

