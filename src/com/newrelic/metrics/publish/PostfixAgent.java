package com.newrelic.metrics.publish;

import com.newrelic.metrics.publish.configuration.ConfigurationException;
import com.newrelic.metrics.publish.util.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostfixAgent extends Agent {

    private static final Logger logger = Logger.getLogger(PostfixAgent.class);
    private static final String QUEUE_SIZE_MSG = "Queue size in kbytes: ";
    private static final String PENDING_EMAILS_MSG = "Number of pending emails: ";
    private static final String POSTQUEUE_REGEX = "(\\d+) (Kbytes|Requests)";

    private static final String GUID = "com.doodle.postfix";
    private static final String VERSION = "1.0.0";
    private final String name;

    public PostfixAgent(String name) throws ConfigurationException {
        super(GUID, VERSION);
        this.name = name;

    }

    @Override
    public String getAgentName() {
        return name;
    }

    @Override
    public void pollCycle() {
        Pattern p = Pattern.compile(POSTQUEUE_REGEX);
        String queue = getPostfixQueue();
        Matcher m = p.matcher(queue);
        Long kbytes = m.find() ? Long.valueOf(m.group()) : 0;
        Long emails = m.find() ? Long.valueOf(m.group()) : 0;

        logger.debug(QUEUE_SIZE_MSG, kbytes);
        reportMetric("Queue size", "Kbytes", kbytes);

        logger.debug(PENDING_EMAILS_MSG, emails);
        reportMetric("Pending emails", "Emails", emails);
    }

    private String getPostfixQueue() {
        String lastLine = "";
        try {
            ProcessBuilder pb = new ProcessBuilder("postqueue", "-p");
            Process process = pb.start();
            if (process.waitFor() == 0) {
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    lastLine = line;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to execute 'postqueue -p'");
        }
        return lastLine;
    }
}
