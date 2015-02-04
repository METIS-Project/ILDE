#!/bin/sh
# 'Uninstalls' the tool adapter for Kaltura:
#	- removes soft links to the start/stop scripts, and to the log file (created with install-manager)
#	- LOG FILE in /var/log/glue is NOT REMOVED
#	- persistence file in /srv/glue is NOT REMOVED

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

ADAPTER_KEY=kaltura
ADAPTER_HOME=$GLUE_HOME/tool_adapters/$ADAPTER_KEY

# remove soft links
rm $ADAPTER_HOME/data/instances.txt
rm $ADAPTER_HOME/log/${ADAPTER_KEY}_adapter.log
rm /usr/bin/start-${ADAPTER_KEY}
rm /usr/bin/stop-${ADAPTER_KEY}
