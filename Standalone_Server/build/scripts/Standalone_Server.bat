@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Standalone_Server startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and STANDALONE_SERVER_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windowz variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\Standalone_Server-1.0.jar;%APP_HOME%\lib\javax.mail-1.5.2.jar;%APP_HOME%\lib\javax.mail-api-1.5.2.jar;%APP_HOME%\lib\mysql-connector-java-5.1.31.jar;%APP_HOME%\lib\httpclient-4.3.3.jar;%APP_HOME%\lib\slf4j-api-1.7.7.jar;%APP_HOME%\lib\slf4j-simple-1.7.7.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\jcl-over-slf4j-1.5.11.jar;%APP_HOME%\lib\activation-1.1.jar;%APP_HOME%\lib\httpcore-4.3.2.jar;%APP_HOME%\lib\commons-logging-1.1.3.jar;%APP_HOME%\lib\commons-codec-1.6.jar

@rem Execute Standalone_Server
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %STANDALONE_SERVER_OPTS%  -classpath "%CLASSPATH%" edu.csci.standalone_server.MultiThreadedHttpServer %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable STANDALONE_SERVER_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%STANDALONE_SERVER_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
