#################################################################################
# Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
# Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
# Valladolid, Spain. https://www.gsic.uva.es/
# 
# This file is part of Basic LTI Adapter.
# 
# Basic LTI Adapter is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or 
# (at your option) any later version.
# 
# Basic LTI Adapter is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
# GNU Affero General Public License for more details.
# 
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#################################################################################

#!/bin/sh
# Stops BasicLTIAdapter

PIDS=`ps -u $USER -o pid -o args | grep basiclti-adapter[.]jar | sed 's/^ *//' | cut -f1 -d' '`
for ID in $PIDS
do
	echo stopping BasicLTIAdapter with PID $ID
	kill -TERM $ID
done
