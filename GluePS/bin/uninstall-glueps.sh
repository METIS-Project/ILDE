#!/bin/sh
# 'Uninstalls' GLUEPSManager:
#	- removes soft links to the start/stop scripts, and to the log file (created with install-manager)
#	- log file in /var/log/glueps is NOT REMOVED
#   - databases content is NOT REMOVED

if [ -z $GLUEPS_HOME ]
then
	GLUEPS_HOME=$(pwd)
	GLUEPS_HOME=$GLUEPS_HOME/../..
	echo Environment value GLUEPS_HOME is not defined
	echo Assuming default value: $GLUEPS_HOME
#	echo "Environment variable GLUEPS_HOME must be defined"
#	exit 1
fi

GM_HOME=$GLUEPS_HOME/manager

# remove soft links
rm $GM_HOME/log/manager.log
rm $GM_HOME/log/managerError.log
rm /usr/bin/start-glueps
rm /usr/bin/stop-glueps
