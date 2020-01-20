@echo off
echo Xerces-Java Build System
echo ------------------------

if "%JAVA_HOME%" == "" goto error

set TOOLS_DIR=../xml-xerces/java/tools
set LOCALCLASSPATH=%JAVA_HOME%\lib\tools.jar;%TOOLS_DIR%\ant.jar;%TOOLS_DIR%\xerces.jar;%TOOLS_DIR%\xalan.jar;%TOOLS_DIR%\stylebook-1.0-b2.jar;%TOOLS_DIR%\style-apachexml.jar;
set ANT_HOME=%TOOLS_DIR%

echo Building with classpath %LOCALCLASSPATH%
echo Starting Ant...
%JAVA_HOME%\bin\java.exe -Dant.home="%ANT_HOME%" -classpath "%LOCALCLASSPATH%" org.apache.tools.ant.Main %1 %2 %3 %4 %5
goto end

:error
echo "ERROR: JAVA_HOME not found in your environment."
echo "Please, set the JAVA_HOME variable in your environment to match the"
echo "location of the Java Virtual Machine you want to use."

:end
set LOCALCLASSPATH=
@echo on
