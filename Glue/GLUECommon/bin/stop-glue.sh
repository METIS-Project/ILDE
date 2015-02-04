#!/bin/sh
# Stops GLUEletManager and every GSIC tool adapter

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

for ad in `ls $GLUE_HOME/tool_adapters`
do
	$GLUE_HOME/tool_adapters/$ad/bin/stop-$ad.sh
done
$GLUE_HOME/manager/bin/stop-gm.sh
