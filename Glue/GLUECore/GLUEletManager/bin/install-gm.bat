:: Registers GLUEletManager as a Windows service

@ECHO ** Running install-gm...
@ECHO OFF

IF DEFINED GLUE_HOME GOTO run
SET GLUE_HOME=%~dp0
IF %GLUE_HOME:~-1%==\ SET GLUE_HOME=%GLUE_HOME:~0,-1%
SET GLUE_HOME=%GLUE_HOME%\..\..
:: ECHO GLUE_HOME is %GLUE_HOME%

:run
:: Basic locations
SET GM_HOME=%GLUE_HOME%\manager
SET CONF_DIR=%GM_HOME%\conf
SET GLUE_LIB_DIR=%GLUE_HOME%\lib
SET GM_LIB_DIR=%GM_HOME%\lib
SET LOG_DIR=%GM_HOME%\log
SET COMMONS_LOG_DIR=%LOG_DIR%\commons-daemon

:: Database related libraries
SET JPA2_API_LIB=%GLUE_LIB_DIR%\dep\eclipselink\jlib\jpa\javax.persistence_2.0.0.v200911271158.jar
SET JPA2_IMP_LIB=%GLUE_LIB_DIR%\dep\eclipselink\jlib\eclipselink.jar
SET JDBC_DIR=%GLUE_LIB_DIR%\dep\jdbc-connector
SET JDBC_DRIVER=
FOR %%j in (%JDBC_DIR%\*.jar) DO SET JDBC_DRIVER=%JDBC_DRIVER%;%%j
IF DEFINED JDBC_DRIVER GOTO run2 
ECHO Jar file(s) for a JDBC driver must be placed at %JDBC_DIR%
EXIT /B

:run2
:: RESTlet related definitions
SET RESTLET_DIR=%GLUE_LIB_DIR%\dep\restlet-jse-2.0.11\lib

SET RESTLET_HTTP_CONN=%RESTLET_DIR%\org.restlet.ext.httpclient.jar;%RESTLET_DIR%\net.jcip.annotations_1.0\net.jcip.annotations.jar;%RESTLET_DIR%\org.apache.commons.codec_1.4\org.apache.commons.codec.jar;%RESTLET_DIR%\org.apache.commons.logging_1.1\org.apache.commons.logging.jar;%RESTLET_DIR%\org.apache.httpclient_4.0\org.apache.httpclient.jar;%RESTLET_DIR%\org.apache.httpcore_4.0\org.apache.httpcore.jar;%RESTLET_DIR%\org.apache.httpmime_4.0\org.apache.httpmime.jar;%RESTLET_DIR%\org.apache.james.mime4j_0.6\org.apache.james.mime4j.jar

SET RESTLET_JETTY_CONN=%RESTLET_DIR%\org.restlet.ext.jetty.jar;%RESTLET_DIR%\org.eclipse.jetty_7.1\org.eclipse.jetty.ajp.jar;%RESTLET_DIR%\org.eclipse.jetty_7.1\org.eclipse.jetty.continuations.jar;%RESTLET_DIR%\org.eclipse.jetty_7.1\org.eclipse.jetty.http.jar;%RESTLET_DIR%\org.eclipse.jetty_7.1\org.eclipse.jetty.io.jar;%RESTLET_DIR%\org.eclipse.jetty_7.1\org.eclipse.jetty.server.jar;%RESTLET_DIR%\org.eclipse.jetty_7.1\org.eclipse.jetty.util.jar;%RESTLET_DIR%\javax.servlet_2.5\javax.servlet.jar

:: Classpath composition - BE CAREFUL: never put ';' before %JDBC_DRIVER% (watch how it was built some lines before)
SET CLASSPATH=%CONF_DIR%;%GM_LIB_DIR%\gluelet-manager.jar;%GLUE_LIB_DIR%\glue-common.jar;%JPA2_API_LIB%;%JPA2_IMP_LIB%%JDBC_DRIVER%;%RESTLET_DIR%\org.restlet.jar;%RESTLET_DIR%\org.restlet.ext.xml.jar;%RESTLET_DIR%\org.restlet.ext.atom.jar;%RESTLET_HTTP_CONN%;%RESTLET_JETTY_CONN%

:: Go!
SET MANUAL_MODE=%1
IF NOT DEFINED MANUAL_MODE GOTO auto
IF %MANUAL_MODE% NEQ manual GOTO auto
%GLUE_HOME%\bin\ServiceInstaller //IS//GLUEletManager --DisplayName=GLUEletManager --Startup=manual --Classpath=%CLASSPATH% --StartMode=Java --StartClass=glue.core.glueletManager.GLUEletManagerServerMain --StartPath=%GLUE_HOME% --LogPath=%COMMONS_LOG_DIR% --LogPrefix=manager --StdOutput=%LOG_DIR%\manager.log --StdError=%LOG_DIR%\manager.log
EXIT /B

:auto
%GLUE_HOME%\bin\ServiceInstaller //IS//GLUEletManager --DisplayName=GLUEletManager --Startup=auto --Classpath=%CLASSPATH% --StartMode=Java --StartClass=glue.core.glueletManager.GLUEletManagerServerMain --StartPath=%GLUE_HOME% --LogPath=%COMMONS_LOG_DIR% --LogPrefix=manager --StdOutput=%LOG_DIR%\manager.log --StdError=%LOG_DIR%\manager.log
