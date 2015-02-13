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
# Starts GLUEletManager and every GSIC tool adapter

if [ -z $GLUE_HOME ]
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

for ad in `ls $GLUE_HOME/tool_adapters`
do
	$GLUE_HOME/tool_adapters/$ad/bin/stop-$ad.sh
	$GLUE_HOME/tool_adapters/$ad/bin/start-$ad.sh
done
$GLUE_HOME/manager/bin/stop-gm.sh
$GLUE_HOME/manager/bin/start-gm.sh
