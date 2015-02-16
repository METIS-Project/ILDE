:: *******************************************************************************
:: Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
:: Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
:: Valladolid, Spain. https://www.gsic.uva.es/
:: 
:: This file is part of Kaltura Adapter.
:: 
:: Kaltura Adapter is free software: you can redistribute it and/or modify
:: it under the terms of the GNU Affero General Public License as published by
:: the Free Software Foundation, either version 3 of the License, or 
:: (at your option) any later version.
:: 
:: Kaltura Adapter is distributed in the hope that it will be useful,
:: but WITHOUT ANY WARRANTY; without even the implied warranty of
:: MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
:: GNU Affero General Public License for more details.
:: 
:: You should have received a copy of the GNU Affero General Public License
:: along with this program.  If not, see <http://www.gnu.org/licenses/>.
:: ******************************************************************************

:: Registers KalturaAdapter as a Windows service

@ECHO ** Running install-kaltura...
@ECHO OFF

IF DEFINED GLUE_HOME GOTO run
SET GLUE_HOME=%~dp0
IF %GLUE_HOME:~-1%==\ SET GLUE_HOME=%GLUE_HOME:~0,-1%
SET GLUE_HOME=%GLUE_HOME%\..\..\..
:: ECHO GLUE_HOME is %GLUE_HOME%

:run
:: Basic locations
SET ADAPTER_KEY=kaltura
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

:: Kaltura related definitions
SET KALTURA_LIB=%ADAPTER_LIB_DIR%\dep\kalturaclient\KalturaClient.jar
::SET KALTURA_EXT_LIB=%ADAPTER_LIB_DIR%\dep\*.jar

:: Classpath composition
SET CLASSPATH=%CONF_DIR%;%ADAPTER_LIB_DIR%\kaltura-adapter.jar;%GLUE_LIB_DIR%\glue-common.jar;%RESTLET_DIR%\org.restlet.jar;%RESTLET_DIR%\org.restlet.ext.xml.jar;%RESTLET_DIR%\org.restlet.ext.atom.jar;%KALTURA_LIB%;%RESTLET_JETTY_CONN%

:: Go!
SET MANUAL_MODE=%1
IF NOT DEFINED MANUAL_MODE GOTO auto
IF %MANUAL_MODE% NEQ manual GOTO auto
%GLUE_HOME%\bin\ServiceInstaller //IS//KalturaAdapter --DisplayName=KalturaAdapter --Startup=manual --Classpath=%CLASSPATH% --StartMode=Java --StartClass=glue.adapters.implementation.kaltura.manager.KalturaAdapterServerMain --StartPath=%DATA_DIR% --LogPath=%COMMONS_LOG_DIR% --LogPrefix=%ADAPTER_KEY%_adapter --StdOutput=%LOG_DIR%\%ADAPTER_KEY%_adapter.log --StdError=%LOG_DIR%\%ADAPTER_KEY%_adapter.log
EXIT /B

:auto
%GLUE_HOME%\bin\ServiceInstaller //IS//KalturaAdapter --DisplayName=KalturaAdapter --Startup=auto --Classpath=%CLASSPATH% --StartMode=Java --StartClass=glue.adapters.implementation.kaltura.manager.KalturaAdapterServerMain --StartPath=%DATA_DIR% --LogPath=%COMMONS_LOG_DIR% --LogPrefix=%ADAPTER_KEY%_adapter --StdOutput=%LOG_DIR%\%ADAPTER_KEY%_adapter.log --StdError=%LOG_DIR%\%ADAPTER_KEY%_adapter.log
