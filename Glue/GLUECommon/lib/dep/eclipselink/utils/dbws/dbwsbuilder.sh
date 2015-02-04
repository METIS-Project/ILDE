#!/bin/sh
. `dirname $0`/setenv.sh 

# User may increase Java memory setting(s) if desired:
JVM_ARGS=-Xmx256m

# Please do not change any of the following lines:
CLASSPATH=`dirname $0`/javax.wsdl_1.6.2.v200806030405.jar:\
`dirname $0`/../../jlib/eclipselink.jar:\
`dirname $0`/eclipselink-dbwsutils.jar:\
${DRIVER_CLASSPATH}

DBWSBUILDER_ARGS="$@"

${JAVA_HOME}/bin/java ${JVM_ARGS} -cp ${CLASSPATH} \
    org.eclipse.persistence.tools.dbws.DBWSBuilder ${DBWSBUILDER_ARGS}
