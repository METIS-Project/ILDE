:: *******************************************************************************
:: Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
:: Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
:: Valladolid, Spain. https://www.gsic.uva.es/
:: 
:: This file is part of Basic LTI Adapter.
:: 
:: Basic LTI Adapter is free software: you can redistribute it and/or modify
:: it under the terms of the GNU Affero General Public License as published by
:: the Free Software Foundation, either version 3 of the License, or 
:: (at your option) any later version.
:: 
:: Basic LTI Adapter is distributed in the hope that it will be useful,
:: but WITHOUT ANY WARRANTY; without even the implied warranty of
:: MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
:: GNU Affero General Public License for more details.
:: 
:: You should have received a copy of the GNU Affero General Public License
:: along with this program.  If not, see <http://www.gnu.org/licenses/>.
:: ******************************************************************************

:: Unregisters BasicLTIAdapter as a Windows service

@ECHO ** Running uninstall-basiclti...
@ECHO OFF

IF DEFINED GLUE_HOME GOTO run
SET GLUE_HOME=%~dp0
IF %GLUE_HOME:~-1%==\ SET GLUE_HOME=%GLUE_HOME:~0,-1%
SET GLUE_HOME=%GLUE_HOME%\..\..\..
:: ECHO GLUE_HOME is %GLUE_HOME%

:run
:: Go!
%GLUE_HOME%\bin\ServiceInstaller //DS//BasicLTIAdapter