#!/bin/sh
# Stops BasicLTIAdapter

PIDS=`ps -u $USER -o pid -o args | grep basiclti-adapter[.]jar | sed 's/^ *//' | cut -f1 -d' '`
for ID in $PIDS
do
	echo stopping BasicLTIAdapter with PID $ID
	kill -TERM $ID
done
