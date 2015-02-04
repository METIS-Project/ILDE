#!/bin/sh
# 'Installs' GLUEPSManager:
#	- creates a log file
#	- creates soft links to the start/stop scripts, and to the log file

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
LOG_DIR=/var/log/glueps

if [ ! -e $LOG_DIR ]
then
	mkdir $LOG_DIR
fi

if [ ! -e $LOG_DIR/manager.log ]
then
	touch $LOG_DIR/manager.log
fi

if [ ! -e $LOG_DIR/managerError.log ]
then
	touch $LOG_DIR/managerError.log
fi

ln -s $LOG_DIR/manager.log $GM_HOME/log/manager.log
ln -s $LOG_DIR/managerError.log $GM_HOME/log/managerError.log
ln -s $GM_HOME/bin/start-glueps.sh /usr/bin/start-glueps
ln -s $GM_HOME/bin/stop-glueps.sh /usr/bin/stop-glueps
