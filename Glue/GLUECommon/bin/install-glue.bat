:: Registers GLUEletManager and every GSIC tool adapter as Windows services

@ECHO ** Running install-GLUE...
@ECHO OFF

IF DEFINED GLUE_HOME GOTO run
SET GLUE_HOME=%~dp0
IF %GLUE_HOME:~-1%==\ SET GLUE_HOME=%GLUE_HOME:~0,-1%
SET GLUE_HOME=%GLUE_HOME:~0,-4%
:: ECHO GLUE_HOME is %GLUE_HOME%

:run
CALL %GLUE_HOME%\manager\bin\install-gm.bat %*
FOR /D %%a IN (%GLUE_HOME%\tool_adapters\*) DO CALL %%a\bin\install-%%~na.bat %*
