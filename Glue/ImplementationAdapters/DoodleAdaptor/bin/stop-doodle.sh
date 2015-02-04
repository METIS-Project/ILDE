#!/bin/sh
# Stops DoodleAdapter

PIDS=`ps -u $USER -o pid -o args | grep doodle-adapter[.]jar | sed 's/^ *//' | cut -f1 -d' '`
for ID in $PIDS
do
	echo stopping DoodleAdapter with PID $ID
	kill -TERM $ID
done
