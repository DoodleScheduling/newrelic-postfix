# Introduction
This plugin monitors a local Postfix queue on linux systems, and sends the metrics to New Relic.

Two metrics are generated:
 * Queue size (in Kbytes)
 * Queue length (number of emails in the queue)

# Installation
Either use the NPI, or follow these steps:
* Unpack the *tar.gz* into any folder of your choice.
* Rename *newrelic.template.json* to *newrelic.json* and add your license.
* Rename *plugin.template.json* to *plugin.json* and tweak the configuration to match your system.
* Create your startup script or use the provided systemd service file

# Configuration
The configuration file is the standard *plugin.json* located in the *config* folder. It has 4 mandatory values:
* name: The name that will appear on the New Relic dashboard.
* command: The command that will be executed to obtain the queue length, typically 'postfix -p'. Only the last line of the standard output is analyzed with the regexes.
* bytesregex: The regex that will be used to get the queue size. Typically 'postfix -p' returns output like '1234 Kbytes', so the default regex is '(\\\d+) Kbytes'. Note the double backslash to make it valid JSON.
* emailsregex: The regex that will be used to get the number of emails in the queue. Typically 'postfix -p' returns output like '100 Requests', so the default regex is '(\\\d+) Requests'. Note the double backslash to make it valid JSON.

# Contribute
We're happy to receive contributions.

* Fork.
* Code.
* Make sure tests pass.
* Make sure gradle createDistributable runs correctly.
* Submit pull request.
