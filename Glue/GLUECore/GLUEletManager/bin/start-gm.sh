#!/bin/sh
# Inits GLUEletManager

if [ -z $GLUE_HOME ] 
then
	echo "Environment variable GLUE_HOME must be defined"
	exit 1
fi

# Basic locations
GM_HOME=$GLUE_HOME/manager
CONF_DIR=$GM_HOME/conf
GLUE_LIB_DIR=$GLUE_HOME/lib
GM_LIB_DIR=$GM_HOME/lib
LOG_DIR=$GM_HOME/log

# Database related libraries
JPA2_API_LIB=$GLUE_LIB_DIR/dep/eclipselink/jlib/jpa/javax.persistence_2.0.0.v200911271158.jar
JPA2_IMP_LIB=$GLUE_LIB_DIR/dep/eclipselink/jlib/eclipselink.jar
JDBC_DIR=$GLUE_LIB_DIR/dep/jdbc-connector
JDBC_DRIVER=''
for JAR in `ls $JDBC_DIR/*.jar`
do
	JDBC_DRIVER=${JDBC_DRIVER}:$JAR
done
if [ -z $JDBC_DRIVER ]
then
	echo "Jar file(s) for a JDBC driver must be placed at $JDBC_DIR"
	exit 1
fi

# RESTlet related definitions
RESTLET_DIR=$GLUE_LIB_DIR/dep/restlet-jse-2.0.11/lib

RESTLET_HTTP_CONN=$RESTLET_DIR/org.restlet.ext.httpclient.jar:$RESTLET_DIR/net.jcip.annotations_1.0/net.jcip.annotations.jar:$RESTLET_DIR/org.apache.commons.codec_1.4/org.apache.commons.codec.jar:$RESTLET_DIR/org.apache.commons.logging_1.1/org.apache.commons.logging.jar:$RESTLET_DIR/org.apache.httpclient_4.0/org.apache.httpclient.jar:$RESTLET_DIR/org.apache.httpcore_4.0/org.apache.httpcore.jar:$RESTLET_DIR/org.apache.httpmime_4.0/org.apache.httpmime.jar:$RESTLET_DIR/org.apache.james.mime4j_0.6/org.apache.james.mime4j.jar

RESTLET_JETTY_CONN=$RESTLET_DIR/org.restlet.ext.jetty.jar:$RESTLET_DIR/org.eclipse.jetty_7.1/org.eclipse.jetty.ajp.jar:$RESTLET_DIR/org.eclipse.jetty_7.1/org.eclipse.jetty.continuations.jar:$RESTLET_DIR/org.eclipse.jetty_7.1/org.eclipse.jetty.http.jar:$RESTLET_DIR/org.eclipse.jetty_7.1/org.eclipse.jetty.io.jar:$RESTLET_DIR/org.eclipse.jetty_7.1/org.eclipse.jetty.server.jar:$RESTLET_DIR/org.eclipse.jetty_7.1/org.eclipse.jetty.util.jar:$RESTLET_DIR/javax.servlet_2.5/javax.servlet.jar


# Classpath composition - BE CAREFUL: never put ':' before $JDBC_DRIVER (watch how it was built some lines before)
CLASSPATH=$CONF_DIR:$GM_LIB_DIR/gluelet-manager.jar:$GLUE_LIB_DIR/glue-common.jar:$JPA2_API_LIB:${JPA2_IMP_LIB}$JDBC_DRIVER:$RESTLET_DIR/org.restlet.jar:$RESTLET_DIR/org.restlet.ext.xml.jar:$RESTLET_DIR/org.restlet.ext.atom.jar:$RESTLET_HTTP_CONN:$RESTLET_JETTY_CONN

# Go!
echo starting GLUEletManager
java -cp $CLASSPATH glue.core.glueletManager.GLUEletManagerServerMain >> $LOG_DIR/manager.log 2>> $LOG_DIR/manager.log &
