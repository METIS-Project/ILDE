#################################################################################
# Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
# Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
# Valladolid, Spain. https://www.gsic.uva.es/
# 
# This file is part of GlueCommon.
# 
# GlueCommon is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or 
# (at your option) any later version.
# 
# GlueCommon is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
# GNU Affero General Public License for more details.
# 
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#################################################################################


#!/bin/sh
# 'Uninstalls' GLUEletManager and every GSIC tool adapter
#	- removes soft links to the start/stop scripts, and to the log file (created with install-manager)
#	- log files in /var/log/glue are NOT REMOVED
#	- persistence files in /srv/glue are NOT REMOVED
#	- databases are NOT CLEANED OR REMOVED

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

for ad in `ls $GLUE_HOME/tool_adapters`
do
	$GLUE_HOME/tool_adapters/$ad/bin/uninstall-$ad.sh
done
$GLUE_HOME/manager/bin/uninstall-gm.sh

rm /usr/bin/start-glue
rm /usr/bin/stop-glue
