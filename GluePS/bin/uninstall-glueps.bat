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