#!/bin/sh
# Stops WookieWidgetsAdapter

PIDS=`ps -u $USER -o pid -o args | grep wookiewidgets-adapter[.]jar | sed 's/^ *//' | cut -f1 -d' '`
for ID in $PIDS
do
	echo stopping WookieWidgetsAdapter with PID $ID
	kill -TERM $ID
done
