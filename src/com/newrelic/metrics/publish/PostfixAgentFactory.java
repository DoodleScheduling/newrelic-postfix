package com.newrelic.metrics.publish;

import com.newrelic.metrics.publish.configuration.ConfigurationException;

import java.util.Map;

public class PostfixAgentFactory extends AgentFactory {
    @Override
    public Agent createConfiguredAgent(Map<String, Object> properties) throws ConfigurationException {
        String name = (String) properties.get("name");

        if (name == null) {
            throw new ConfigurationException("'name' cannot be null.");
        }
        return new PostfixAgent(name);
    }

}
