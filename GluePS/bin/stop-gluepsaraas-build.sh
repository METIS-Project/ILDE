#!/bin/sh
# Stops GLUEPSManager

PIDS=`ps -u $USER -o pid -o args | grep java | grep gluepsaraas | sed 's/^ *//' | cut -f1 -d' '`
for ID in $PIDS
do
	echo killing GLUEPSARAASManager with PID $ID
	kill -TERM $ID
done
