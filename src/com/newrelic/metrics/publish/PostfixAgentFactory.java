package com.newrelic.metrics.publish;

import com.newrelic.metrics.publish.configuration.ConfigurationException;

import java.util.Arrays;
import java.util.Map;

public class PostfixAgentFactory extends AgentFactory {
    @Override
    public Agent createConfiguredAgent(Map<String, Object> properties) throws ConfigurationException {
        String name = (String) properties.get("name");
        String command = (String) properties.get("command");
        String bytesregex = (String) properties.get("bytesregex");
        String emailsregex = (String) properties.get("emailsregex");

        if (name == null || command == null || bytesregex == null || emailsregex == null) {
            throw new ConfigurationException("'name', 'command', 'bytesregex' and 'emailsregex' cannot be null.");
        }
        return new PostfixAgent(name, Arrays.asList(command.split("\\s")), bytesregex, emailsregex);
    }
}
