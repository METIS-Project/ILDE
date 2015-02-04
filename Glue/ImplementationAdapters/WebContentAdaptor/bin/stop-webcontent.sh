#!/bin/sh
# Stops WebContentAdapter

PIDS=`ps -u $USER -o pid -o args | grep webcontent-adapter[.]jar | sed 's/^ *//' | cut -f1 -d' '`
for ID in $PIDS
do
	echo stopping WebContentAdapter with PID $ID
	kill -TERM $ID
done
