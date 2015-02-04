#!/bin/sh
# Stops GLUEletManager

PIDS=`ps -u $USER -o pid -o args | grep gluelet-manager[.]jar | sed 's/^ *//' | cut -f1 -d' '`
for ID in $PIDS
do
	echo killing GLUEletManager with PID $ID
	kill -TERM $ID
done
