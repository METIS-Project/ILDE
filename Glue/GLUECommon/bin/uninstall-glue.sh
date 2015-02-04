#!/bin/sh
# 'Uninstalls' GLUEletManager and every GSIC tool adapter
#	- removes soft links to the start/stop scripts, and to the log file (created with install-manager)
#	- log files in /var/log/glue are NOT REMOVED
#	- persistence files in /srv/glue are NOT REMOVED
#	- databases are NOT CLEANED OR REMOVED

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

for ad in `ls $GLUE_HOME/tool_adapters`
do
	$GLUE_HOME/tool_adapters/$ad/bin/uninstall-$ad.sh
done
$GLUE_HOME/manager/bin/uninstall-gm.sh

rm /usr/bin/start-glue
rm /usr/bin/stop-glue
