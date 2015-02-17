:: *******************************************************************************
:: Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
:: Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
:: Valladolid, Spain. https://www.gsic.uva.es/
:: 
:: This file is part of Glue!PS.
:: 
:: Glue!PS is free software: you can redistribute it and/or modify
:: it under the terms of the GNU Affero General Public License as published by
:: the Free Software Foundation, either version 3 of the License, or 
:: (at your option) any later version.
:: 
:: Glue!PS is distributed in the hope that it will be useful,
:: but WITHOUT ANY WARRANTY; without even the implied warranty of
:: MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
:: GNU Affero General Public License for more details.
:: 
:: You should have received a copy of the GNU Affero General Public License
:: along with this program.  If not, see <http://www.gnu.org/licenses/>.
:: ******************************************************************************

:: Registers GLUEPSManager as a Windows service

@ECHO ** Running install-glueps...
@ECHO OFF

IF DEFINED GLUEPS_HOME GOTO run
SET GLUEPS_HOME=%~dp0
IF %GLUEPS_HOME:~-1%==\ SET GLUEPS_HOME=%GLUEPS_HOME:~0,-1%
SET GLUEPS_HOME=%GLUEPS_HOME%\..\..
:: ECHO GLUEPS_HOME is %GLUEPS_HOME%

:run
:: Basic locations
SET GM_HOME=%GLUEPS_HOME%\manager
SET CONF_DIR=%GM_HOME%\conf
SET GLUEPS_LIB_DIR=%GLUEPS_HOME%\lib
SET GM_LIB_DIR=%GM_HOME%\lib
SET GM_LIB_TEST_DIR=%GM_HOME%\lib-test
SET LOG_DIR=%GM_HOME%\log
SET COMMONS_LOG_DIR=%LOG_DIR%\commons-daemon

:run2
:: Classpath composition
set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;%GLUEPS_LIB_DIR%\glue-common.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\glueps-manager.jar
set CLASSPATH=%CLASSPATH%;%CONF_DIR%
:: All the necessary libraries must be included here
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\activation.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\javax.mail.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\log4j-1.2.16.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\commons-codec-1.4.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\mailapi.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\commons-collections-3.2.1.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\commons-email-1.3.1.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\mysql-connector-java-5.1.18-bin.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\commons-fileupload-1.2.2.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\net.jcip.annotations.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\commons-io-2.0.1.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\commons-lang3-3.1.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.apache.james.mime4j.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\commons-logging-1.1.1.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\commons-validator-1.4.0.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.eclipse.jetty.ajp.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\dom4j-1.6.1.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.eclipse.jetty.continuations.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\eclipselink.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.eclipse.jetty.http.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\exist.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.eclipse.jetty.io.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\exist-optional.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.eclipse.jetty.server.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\gson-2.1.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.eclipse.jetty.util.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\httpclient-4.1.3.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.json.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\httpclient-cache-4.1.3.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.restlet.ext.atom.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\httpcore-4.1.4.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.restlet.ext.crypto.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\httpmime-4.1.3.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.restlet.ext.fileupload.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\j2ee-1.4.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.restlet.ext.jetty.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\javax.persistence_2.0.3.v201010191057.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.restlet.ext.json.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\javax.servlet.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\persistence.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.restlet.ext.xml.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\jaxb1-impl.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\org.restlet.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\jaxb-api.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\ws-commons-util-1.0.2.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\jaxb-impl.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\xml-apis-1.3.04.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\jaxb-xjc.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\jdo-api-3.0.1.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\xml-apis.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\jdom.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\xmldb.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\jsr173_1.0_api.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\xmlrpc-client-3.1.2.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\jug-1.1.2.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\xmlrpc-common-3.1.2.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_DIR%\log4j-1.2.11.jar
set CLASSPATH=%CLASSPATH%;%GM_LIB_TEST_DIR%\selenium-server-standalone-2.31.0.jar

:: Go!
SET MANUAL_MODE=%1
IF NOT DEFINED MANUAL_MODE GOTO auto
IF %MANUAL_MODE% NEQ manual GOTO auto
%GLUEPS_HOME%\bin\ServiceInstaller //IS//GLUEPSManager --DisplayName=GLUEPSManager --Startup=manual --Classpath=%CLASSPATH% --StartMode=Java --StartClass=glueps.core.gluepsManager.GLUEPSManagerServerMain --StartPath=%GM_HOME% --LogPath=%COMMONS_LOG_DIR% --LogPrefix=manager --StdOutput=%LOG_DIR%\manager.log --StdError=%LOG_DIR%\managerError.log
EXIT /B

:auto
%GLUEPS_HOME%\bin\ServiceInstaller //IS//GLUEPSManager --DisplayName=GLUEPSManager --Startup=auto --Classpath=%CLASSPATH% --StartMode=Java --StartClass=glueps.core.gluepsManager.GLUEPSManagerServerMain --StartPath=%GM_HOME% --LogPath=%COMMONS_LOG_DIR% --LogPrefix=manager --StdOutput=%LOG_DIR%\manager.log --StdError=%LOG_DIR%\managerError.log --JvmOptions=-Dfile.encoding=UTF8
