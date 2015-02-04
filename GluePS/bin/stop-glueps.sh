#!/bin/sh
# Stops GLUEPSManager

PIDS=`ps -u $USER -o pid -o args | grep glueps-manager[.]jar | sed 's/^ *//' | cut -f1 -d' '`
for ID in $PIDS
do
	echo killing GLUEPSManager with PID $ID
	kill -TERM $ID
done
