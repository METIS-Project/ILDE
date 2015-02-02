#!/bin/sh
# Stops GDataAdapter

PIDS=`ps -u $USER -o pid -o args | grep gdata3-adapter[.]jar | sed 's/^ *//' | cut -f1 -d' '`
for ID in $PIDS
do
	echo stopping GDataAdapter with PID $ID
	kill -TERM $ID
done
