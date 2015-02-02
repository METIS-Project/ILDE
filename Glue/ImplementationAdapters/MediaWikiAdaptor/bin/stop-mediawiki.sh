#!/bin/sh
# Stops MediaWikiAdapter

PIDS=`ps -u $USER -o pid -o args | grep mediawiki-adapter[.]jar | sed 's/^ *//' | cut -f1 -d' '`
for ID in $PIDS
do
	echo stopping MediaWikiAdapter with PID $ID
	kill -TERM $ID
done
