:: Registers DoodleAdapter as a Windows service

@ECHO ** Running install-doodle...
@ECHO OFF

IF DEFINED GLUE_HOME GOTO run
SET GLUE_HOME=%~dp0
IF %GLUE_HOME:~-1%==\ SET GLUE_HOME=%GLUE_HOME:~0,-1%
SET GLUE_HOME=%GLUE_HOME%\..\..\..
:: ECHO GLUE_HOME is %GLUE_HOME%

:run
:: Basic locations
SET ADAPTER_KEY=doodle
SET ADAPTER_HOME=%GLUE_HOME%\tool_adapters\%ADAPTER_KEY%
SET CONF_DIR=%ADAPTER_HOME%\conf
SET DATA_DIR=%ADAPTER_HOME%\data
SET GLUE_LIB_DIR=%GLUE_HOME%\lib
SET ADAPTER_LIB_DIR=%ADAPTER_HOME%\lib
SET LOG_DIR=%ADAPTER_HOME%\log
SET COMMONS_LOG_DIR=%LOG_DIR%\commons-daemon

:: RESTlet related definitions
SET RESTLET_DIR=%GLUE_LIB_DIR%\dep\restlet-jse-2.0.11\lib

SET RESTLET_JETTY_CONN=%RESTLET_DIR%\org.restlet.ext.jetty.jar;%RESTLET_DIR%\org.eclipse.jetty_7.1\org.eclipse.jetty.ajp.jar;%RESTLET_DIR%\org.eclipse.jetty_7.1\org.eclipse.jetty.continuations.jar;%RESTLET_DIR%\org.eclipse.jetty_7.1\org.eclipse.jetty.http.jar;%RESTLET_DIR%\org.eclipse.jetty_7.1\org.eclipse.jetty.io.jar;%RESTLET_DIR%\org.eclipse.jetty_7.1\org.eclipse.jetty.server.jar;%RESTLET_DIR%\org.eclipse.jetty_7.1\org.eclipse.jetty.util.jar;%RESTLET_DIR%\javax.servlet_2.5\javax.servlet.jar

:: Doodle related definitions
::SET DOODLE_LIB=

:: Classpath composition
SET CLASSPATH=%CONF_DIR%;%ADAPTER_LIB_DIR%\doodle-adapter.jar;%GLUE_LIB_DIR%\glue-common.jar;%RESTLET_DIR%\org.restlet.jar;%RESTLET_DIR%\org.restlet.ext.xml.jar;%RESTLET_DIR%\org.restlet.ext.atom.jar;%RESTLET_JETTY_CONN%

:: Go!
SET MANUAL_MODE=%1
IF NOT DEFINED MANUAL_MODE GOTO auto
IF %MANUAL_MODE% NEQ manual GOTO auto
%GLUE_HOME%\bin\ServiceInstaller //IS//DoodleAdapter --DisplayName=DoodleAdapter --Startup=manual --Classpath=%CLASSPATH% --StartMode=Java --StartClass=glue.adapters.implementation.doodle.manager.DoodleAdapterServerMain --StartPath=%DATA_DIR% --LogPath=%COMMONS_LOG_DIR% --LogPrefix=%ADAPTER_KEY%_adapter --StdOutput=%LOG_DIR%\%ADAPTER_KEY%_adapter.log --StdError=%LOG_DIR%\%ADAPTER_KEY%_adapter.log
EXIT /B

:auto
%GLUE_HOME%\bin\ServiceInstaller //IS//DoodleAdapter --DisplayName=DoodleAdapter --Startup=auto --Classpath=%CLASSPATH% --StartMode=Java --StartClass=glue.adapters.implementation.doodle.manager.DoodleAdapterServerMain --StartPath=%DATA_DIR% --LogPath=%COMMONS_LOG_DIR% --LogPrefix=%ADAPTER_KEY%_adapter --StdOutput=%LOG_DIR%\%ADAPTER_KEY%_adapter.log --StdError=%LOG_DIR%\%ADAPTER_KEY%_adapter.log
