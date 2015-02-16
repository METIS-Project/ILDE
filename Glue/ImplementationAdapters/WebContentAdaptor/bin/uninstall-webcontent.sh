#################################################################################
# Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
# Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
# Valladolid, Spain. https://www.gsic.uva.es/
# 
# This file is part of Web Content Adapter.
# 
# Web Content Adapter is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or 
# (at your option) any later version.
# 
# Web Content Adapter is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
# GNU Affero General Public License for more details.
# 
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#################################################################################

#!/bin/sh
# 'Uninstalls' the tool adapter for web content:
#	- removes soft links to the start/stop scripts, and to the log file (created with install-manager)
#	- LOG FILE in /var/log/glue is NOT REMOVED
#	- persistence file in /srv/glue is NOT REMOVED

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

ADAPTER_KEY=webcontent
ADAPTER_HOME=$GLUE_HOME/tool_adapters/$ADAPTER_KEY

# remove soft links
rm $ADAPTER_HOME/data/instances.txt
rm $ADAPTER_HOME/log/${ADAPTER_KEY}_adapter.log
rm /usr/bin/start-${ADAPTER_KEY}
rm /usr/bin/stop-${ADAPTER_KEY}
