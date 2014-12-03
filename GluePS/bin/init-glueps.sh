#!/bin/sh
export GLUEPSCLASSPATH="."
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./../lib/glue-common.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/glueps-manager.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./conf"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/activation.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/log4j-1.2.16.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/commons-codec-1.4.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/mailapi.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/commons-collections-3.2.1.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/commons-email-1.3.1.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/mysql-connector-java-5.1.18-bin.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/commons-fileupload-1.2.2.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/net.jcip.annotations.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/commons-io-2.0.1.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.apache.james.mime4j.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/commons-logging-1.1.1.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/commons-validator-1.4.0.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.eclipse.jetty.ajp.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/dom4j-1.6.1.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.eclipse.jetty.continuations.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/eclipselink.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.eclipse.jetty.http.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/exist.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.eclipse.jetty.io.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/exist-optional.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.eclipse.jetty.server.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/gson-2.1.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.eclipse.jetty.util.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/httpclient-4.1.3.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.json.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/httpclient-cache-4.1.3.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.restlet.ext.atom.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/httpcore-4.1.4.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.restlet.ext.crypto.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/httpmime-4.1.3.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.restlet.ext.fileupload.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/j2ee-1.4.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.restlet.ext.jetty.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/javax.persistence_2.0.3.v201010191057.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.restlet.ext.json.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/javax.servlet.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.restlet.ext.xml.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/jaxb1-impl.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/org.restlet.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/jaxb-api.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/ws-commons-util-1.0.2.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/jaxb-impl.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/xml-apis-1.3.04.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/jaxb-xjc.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/xml-apis.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/jdom.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/jsoup-1.8.1.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/xmldb.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/jsr173_1.0_api.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/xmlrpc-client-3.1.2.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/jug-1.1.2.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/xmlrpc-common-3.1.2.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib/log4j-1.2.11.jar"
export GLUEPSCLASSPATH=$GLUEPSCLASSPATH:"./lib-test/selenium-server-standalone-2.31.0.jar"
nohup java -cp $GLUEPSCLASSPATH glueps.core.gluepsManager.GLUEPSManagerServerMain >/tmp/glueps.log &


