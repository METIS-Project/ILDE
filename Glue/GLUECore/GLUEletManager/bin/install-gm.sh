#################################################################################
# Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
# Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
# Valladolid, Spain. https://www.gsic.uva.es/
# 
# This file is part of GlueletManager.
# 
# GlueletManager is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or 
# (at your option) any later version.
# 
# GlueletManager is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
# GNU Affero General Public License for more details.
# 
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#################################################################################


#!/bin/sh
# 'Installs' GLUEletManager:
#	- creates a log file
#	- creates soft links to the start/stop scripts, and to the log file

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

GM_HOME=$GLUE_HOME/manager
LOG_DIR=/var/log/glue

if [ ! -e $LOG_DIR ]
then
	mkdir $LOG_DIR
fi

if [ ! -e $LOG_DIR/manager.log ]
then
	> $LOG_DIR/manager.log
fi

ln -s $LOG_DIR/manager.log $GM_HOME/log/manager.log
ln -s $GM_HOME/bin/start-gm.sh /usr/bin/start-gm
ln -s $GM_HOME/bin/stop-gm.sh /usr/bin/stop-gm
