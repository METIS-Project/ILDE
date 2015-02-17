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

:: Unregisters GLUEPSManager as a Windows service

@ECHO ** Running uninstall-glueps...
@ECHO OFF

IF DEFINED GLUEPS_HOME GOTO run
SET GLUEPS_HOME=%~dp0
IF %GLUEPS_HOME:~-1%==\ SET GLUEPS_HOME=%GLUEPS_HOME:~0,-1%
SET GLUEPS_HOME=%GLUEPS_HOME%\..\..
:: ECHO GLUEPS_HOME is %GLUEPS_HOME%

:run
:: Go!
%GLUEPS_HOME%\bin\ServiceInstaller //DS//GLUEPSManager