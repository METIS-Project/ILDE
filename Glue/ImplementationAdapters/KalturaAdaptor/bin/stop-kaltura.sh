#!/bin/sh
# Stops KalturaAdapter

PIDS=`ps -u $USER -o pid -o args | grep kaltura-adapter[.]jar | sed 's/^ *//' | cut -f1 -d' '`
for ID in $PIDS
do
	echo stopping KalturaAdapter with PID $ID
	kill -TERM $ID
done
