#################################################################################
# Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
# Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
# Valladolid, Spain. https://www.gsic.uva.es/
# 
# This file is part of Glue!PS.
# 
# Glue!PS is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or 
# (at your option) any later version.
# 
# Glue!PS is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
# GNU Affero General Public License for more details.
# 
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#################################################################################

#!/bin/sh
# 'Uninstalls' GLUEPSManager:
#	- removes soft links to the start/stop scripts, and to the log file (created with install-manager)
#	- log file in /var/log/glueps is NOT REMOVED
#   - databases content is NOT REMOVED

if [ -z $GLUEPS_HOME ]
then
	GLUEPS_HOME=$(pwd)
	GLUEPS_HOME=$GLUEPS_HOME/../..
	echo Environment value GLUEPS_HOME is not defined
	echo Assuming default value: $GLUEPS_HOME
#	echo "Environment variable GLUEPS_HOME must be defined"
#	exit 1
fi

GM_HOME=$GLUEPS_HOME/manager

# remove soft links
rm $GM_HOME/log/manager.log
rm $GM_HOME/log/managerError.log
rm /usr/bin/start-glueps
rm /usr/bin/stop-glueps
