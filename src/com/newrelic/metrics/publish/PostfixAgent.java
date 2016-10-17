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
    private static final String POSTQUEUE_REGEX_BYTES = "(\\d+) Kbytes";
    private static final String POSTQUEUE_REGEX_REQUESTS = "(\\d+) Requests";

    private static final String GUID = "com.doodle.postfix";
    private static final String VERSION = "1.0.0";
    private final String name;
    private final static Pattern patternBytes = Pattern.compile(POSTQUEUE_REGEX_BYTES);
    private final static Pattern patternRequests = Pattern.compile(POSTQUEUE_REGEX_REQUESTS);

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
        String queue = getPostfixQueue();
        logger.debug("Parsing output: " + queue);
        Matcher matcherBytes = patternBytes.matcher(queue);
        Matcher matcherRequests = patternRequests.matcher(queue);
        Long kbytes = matcherBytes.find() ? Long.valueOf(matcherBytes.group(1)) : 0;
        Long emails = matcherRequests.find() ? Long.valueOf(matcherRequests.group(1)) : 0;

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
            String line;

            // Read standard output, and save only the last line
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null) {
                lastLine = line;
            }

            // Read standard error
            br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder output = new StringBuilder();
            while ((line = br.readLine()) != null) {
                output.append(line);
                output.append(System.getProperty("line.separator"));
            }

            // Wait for the command to finish
            int returnCode = process.waitFor();
            if (returnCode != 0) {
                logger.error("Command returned code: " + returnCode);
                if (output.length() > 0) {
                    logger.error("Standard error:");
                    logger.error(output);
                }
                throw new RuntimeException();
            }
        } catch (Exception e) {
            logger.error("Failed to execute 'postqueue -p'");
        }
        return lastLine;
    }
}
