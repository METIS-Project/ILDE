#!/bin/sh
# 'Uninstalls' GLUEletManager:
#	- removes soft links to the start/stop scripts, and to the log file (created with install-manager)
#	- log file in /var/log/glue is NOT REMOVED
#   - databases content is NOT REMOVED

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

GM_HOME=$GLUE_HOME/manager

# remove soft links
rm $GM_HOME/log/manager.log
rm /usr/bin/start-gm
rm /usr/bin/stop-gm
