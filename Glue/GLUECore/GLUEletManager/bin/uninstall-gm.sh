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
# 'Uninstalls' GLUEletManager:
#	- removes soft links to the start/stop scripts, and to the log file (created with install-manager)
#	- log file in /var/log/glue is NOT REMOVED
#   - databases content is NOT REMOVED

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

GM_HOME=$GLUE_HOME/manager

# remove soft links
rm $GM_HOME/log/manager.log
rm /usr/bin/start-gm
rm /usr/bin/stop-gm
