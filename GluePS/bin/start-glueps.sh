#!/bin/sh
# Inits GLUEPSManager

if [ -z $GLUEPS_HOME ] 
then
	GLUEPS_HOME=$(pwd)
	GLUEPS_HOME=$GLUEPS_HOME/../..
	echo Environment value GLUEPS_HOME is not defined
	echo Assuming default value: $GLUEPS_HOME
#	echo "Environment variable GLUEPS_HOME must be defined"
#	exit 1
fi

# Basic locations
GM_HOME=$GLUEPS_HOME/manager
CONF_DIR=$GM_HOME/conf
GLUEPS_LIB_DIR=$GLUEPS_HOME/lib
GM_LIB_DIR=$GM_HOME/lib
GM_LIB_TEST_DIR=$GM_HOME/lib-test
LOG_DIR=$GM_HOME/log

# Classpath composition
CLASSPATH="."
CLASSPATH=$CLASSPATH:$GLUEPS_LIB_DIR/glue-common.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/glueps-manager.jar
CLASSPATH=$CLASSPATH:$CONF_DIR
# All the necessary libraries must be included here
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/bucket.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/activation.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/javax.mail.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/log4j-1.2.16.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/commons-codec-1.4.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/mailapi.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/commons-collections-3.2.1.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/commons-email-1.3.1.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/mysql-connector-java-5.1.18-bin.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/commons-fileupload-1.2.2.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/net.jcip.annotations.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/commons-io-2.0.1.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/commons-lang3-3.1.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.apache.james.mime4j.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/commons-logging-1.1.1.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/commons-validator-1.4.0.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.eclipse.jetty.ajp.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/dom4j-1.6.1.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.eclipse.jetty.continuations.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/eclipselink.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.eclipse.jetty.http.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/exist.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.eclipse.jetty.io.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/exist-optional.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.eclipse.jetty.server.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/gson-2.1.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/guava-14.0.1.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.eclipse.jetty.util.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/httpclient-4.1.3.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.json.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/httpclient-cache-4.1.3.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.restlet.ext.atom.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/httpcore-4.1.4.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.restlet.ext.crypto.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/httpmime-4.1.3.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.restlet.ext.fileupload.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/j2ee-1.4.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.restlet.ext.jetty.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/javax.persistence_2.0.3.v201010191057.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.restlet.ext.json.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/javax.servlet.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.restlet.ext.xml.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/jaxb1-impl.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/org.restlet.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/persistence.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/jaxb-api.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/ws-commons-util-1.0.2.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/jaxb-impl.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/xml-apis-1.3.04.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/jaxb-xjc.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/jdo-api-3.0.1.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/xml-apis.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/jdom.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/jsoup-1.8.1.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/xmldb.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/jsr173_1.0_api.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/xmlrpc-client-3.1.2.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/jug-1.1.2.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/xmlrpc-common-3.1.2.jar
CLASSPATH=$CLASSPATH:$GM_LIB_DIR/log4j-1.2.11.jar
CLASSPATH=$CLASSPATH:$GM_LIB_TEST_DIR/selenium-server-standalone-2.31.0.jar
# echo $CLASSPATH

# We need to change to the manager directory to launch the server. Otherwise app.properties will not be found
cd $GM_HOME

# Go!
#Export display to may run firefox in the Moodle dynamic deploy
export DISPLAY=:99
java -cp $CLASSPATH glueps.core.gluepsManager.GLUEPSManagerServerMain >> $LOG_DIR/manager.log 2>> $LOG_DIR/managerError.log &
