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
# Inits WebContentAdapter

if [ -z $GLUE_HOME ] 
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

# Basic locations
ADAPTER_KEY=webcontent
ADAPTER_HOME=$GLUE_HOME/tool_adapters/$ADAPTER_KEY
CONF_DIR=$ADAPTER_HOME/conf
GLUE_LIB_DIR=$GLUE_HOME/lib
ADAPTER_LIB_DIR=$ADAPTER_HOME/lib
LOG_DIR=$ADAPTER_HOME/log
DATA_DIR=$ADAPTER_HOME/data
BACK_DIR=`pwd`

# RESTlet related definitions
RESTLET_DIR=$GLUE_LIB_DIR/dep/restlet-jse-2.0.11/lib

RESTLET_JETTY_CONN=$RESTLET_DIR/org.restlet.ext.jetty.jar:$RESTLET_DIR/org.eclipse.jetty_7.1/org.eclipse.jetty.ajp.jar:$RESTLET_DIR/org.eclipse.jetty_7.1/org.eclipse.jetty.continuations.jar:$RESTLET_DIR/org.eclipse.jetty_7.1/org.eclipse.jetty.http.jar:$RESTLET_DIR/org.eclipse.jetty_7.1/org.eclipse.jetty.io.jar:$RESTLET_DIR/org.eclipse.jetty_7.1/org.eclipse.jetty.server.jar:$RESTLET_DIR/org.eclipse.jetty_7.1/org.eclipse.jetty.util.jar:$RESTLET_DIR/javax.servlet_2.5/javax.servlet.jar

# Classpath composition
CLASSPATH=$CONF_DIR:$ADAPTER_LIB_DIR/webcontent-adapter.jar:$GLUE_LIB_DIR/glue-common.jar:$RESTLET_DIR/org.restlet.jar:$RESTLET_DIR/org.restlet.ext.xml.jar:$RESTLET_DIR/org.restlet.ext.atom.jar:$RESTLET_JETTY_CONN

# Go!
cd $DATA_DIR
echo starting WebContentAdapter
java -cp $CLASSPATH glue.adapters.implementation.webcontent.manager.WebContentAdapterServerMain >> $LOG_DIR/${ADAPTER_KEY}_adapter.log 2>> $LOG_DIR/${ADAPTER_KEY}_adapter.log &
cd $BACK_DIR
