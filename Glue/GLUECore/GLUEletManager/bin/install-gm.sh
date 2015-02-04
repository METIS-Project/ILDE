#!/bin/sh
# 'Installs' GLUEletManager:
#	- creates a log file
#	- creates soft links to the start/stop scripts, and to the log file

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

GM_HOME=$GLUE_HOME/manager
LOG_DIR=/var/log/glue

if [ ! -e $LOG_DIR ]
then
	mkdir $LOG_DIR
fi

if [ ! -e $LOG_DIR/manager.log ]
then
	> $LOG_DIR/manager.log
fi

ln -s $LOG_DIR/manager.log $GM_HOME/log/manager.log
ln -s $GM_HOME/bin/start-gm.sh /usr/bin/start-gm
ln -s $GM_HOME/bin/stop-gm.sh /usr/bin/stop-gm
