#################################################################################
# Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
# Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
# Valladolid, Spain. https://www.gsic.uva.es/
# 
# This file is part of MediaWiki Adapter.
# 
# MediaWiki Adapter is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or 
# (at your option) any later version.
# 
# MediaWiki Adapter is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
# GNU Affero General Public License for more details.
# 
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#################################################################################


#!/bin/sh
# 'Installs' the tool adapter for MediaWiki:
#	- creates a log file
#	- creates a file to provide persistence
#	- creates soft links to the start/stop scripts, and to the log file

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

ADAPTER_KEY=mediawiki
ADAPTER_HOME=$GLUE_HOME/tool_adapters/$ADAPTER_KEY

# creates log file
LOG_DIR=/var/log/glue
if [ ! -e $LOG_DIR ]
then
	mkdir -p $LOG_DIR
fi

if [ ! -e $LOG_DIR/tool_adapters_${ADAPTER_KEY}.log ]
then
	> $LOG_DIR/tool_adapters_${ADAPTER_KEY}.log
fi

# creates persistence file
DATA_DIR=/srv/glue/tool_adapters/$ADAPTER_KEY
if [ ! -e $DATA_DIR ]
then
	mkdir -p $DATA_DIR
fi

if [ ! -e $DATA_DIR/instances.txt ]
then
	> $DATA_DIR/instances.txt
fi

# creates symbolic links
ln -s $DATA_DIR/instances.txt $ADAPTER_HOME/data/instances.txt
ln -s $LOG_DIR/tool_adapters_${ADAPTER_KEY}.log $ADAPTER_HOME/log/${ADAPTER_KEY}_adapter.log
ln -s $ADAPTER_HOME/bin/start-${ADAPTER_KEY}.sh /usr/bin/start-$ADAPTER_KEY
ln -s $ADAPTER_HOME/bin/stop-${ADAPTER_KEY}.sh /usr/bin/stop-$ADAPTER_KEY
