#!/bin/sh
# Inits DoodleAdapter

if [ -z $GLUE_HOME ] 
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

# Basic locations
ADAPTER_KEY=doodle
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

# Doodle related definitions
#DOODLE_LIB=

# Classpath composition
CLASSPATH=$CONF_DIR:$ADAPTER_LIB_DIR/doodle-adapter.jar:$GLUE_LIB_DIR/glue-common.jar:$RESTLET_DIR/org.restlet.jar:$RESTLET_DIR/org.restlet.ext.xml.jar:$RESTLET_DIR/org.restlet.ext.atom.jar:$RESTLET_JETTY_CONN

# Go!
cd $DATA_DIR
echo starting DoodleAdapter
java -cp $CLASSPATH glue.adapters.implementation.doodle.manager.DoodleAdapterServerMain >> $LOG_DIR/${ADAPTER_KEY}_adapter.log 2>> $LOG_DIR/${ADAPTER_KEY}_adapter.log &
cd $BACK_DIR
