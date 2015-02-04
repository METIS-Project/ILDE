:: Unregisters DoodleAdapter as a Windows service

@ECHO ** Running uninstall-doodle...
@ECHO OFF

IF DEFINED GLUE_HOME GOTO run
SET GLUE_HOME=%~dp0
IF %GLUE_HOME:~-1%==\ SET GLUE_HOME=%GLUE_HOME:~0,-1%
SET GLUE_HOME=%GLUE_HOME%\..\..\..
:: ECHO GLUE_HOME is %GLUE_HOME%

:run
:: Go!
%GLUE_HOME%\bin\ServiceInstaller //DS//DoodleAdapter