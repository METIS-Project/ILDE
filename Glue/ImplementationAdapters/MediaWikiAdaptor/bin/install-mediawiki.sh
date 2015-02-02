#!/bin/sh
# 'Installs' the tool adapter for MediaWiki:
#	- creates a log file
#	- creates a file to provide persistence
#	- creates soft links to the start/stop scripts, and to the log file

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

ADAPTER_KEY=mediawiki
ADAPTER_HOME=$GLUE_HOME/tool_adapters/$ADAPTER_KEY

# creates log file
LOG_DIR=/var/log/glue
if [ ! -e $LOG_DIR ]
then
	mkdir -p $LOG_DIR
fi

if [ ! -e $LOG_DIR/tool_adapters_${ADAPTER_KEY}.log ]
then
	> $LOG_DIR/tool_adapters_${ADAPTER_KEY}.log
fi

# creates persistence file
DATA_DIR=/srv/glue/tool_adapters/$ADAPTER_KEY
if [ ! -e $DATA_DIR ]
then
	mkdir -p $DATA_DIR
fi

if [ ! -e $DATA_DIR/instances.txt ]
then
	> $DATA_DIR/instances.txt
fi

# creates symbolic links
ln -s $DATA_DIR/instances.txt $ADAPTER_HOME/data/instances.txt
ln -s $LOG_DIR/tool_adapters_${ADAPTER_KEY}.log $ADAPTER_HOME/log/${ADAPTER_KEY}_adapter.log
ln -s $ADAPTER_HOME/bin/start-${ADAPTER_KEY}.sh /usr/bin/start-$ADAPTER_KEY
ln -s $ADAPTER_HOME/bin/stop-${ADAPTER_KEY}.sh /usr/bin/stop-$ADAPTER_KEY
