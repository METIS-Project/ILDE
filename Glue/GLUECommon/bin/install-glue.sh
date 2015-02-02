#!/bin/sh
# 'Installs' GLUEletManager and every GSIC tool adapter
#	- create a log file per component
#	- creates persistence files for tool adapters
#	- create soft links to the start/stop scripts, and to the log file (removable with uninstall-manager):: Registers  as Windows services

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

$GLUE_HOME/manager/bin/install-gm.sh
for ad in `ls $GLUE_HOME/tool_adapters`
do
	$GLUE_HOME/tool_adapters/$ad/bin/install-$ad.sh
done

ln -s $GLUE_HOME/bin/start-glue.sh /usr/bin/start-glue
ln -s $GLUE_HOME/bin/stop-glue.sh /usr/bin/stop-glue
