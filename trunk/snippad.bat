@echo off
set LIBRARY_PATH=lib
set SNIPPAD_JAR=dist\snippad.jar
set CLASSPATH=

for %%A in ("%LIBRARY_PATH%\*") do call :APPEND %%A

set CLASSPATH=%SNIPPAD_JAR%;%CLASSPATH%

set JAVA=%JAVA_HOME%\bin\java
set NATIVE_LIB=%LIBRARY_PATH%\windows

echo %CLASSPATH%
%JAVA% -cp %CLASSPATH% -Djava.library.path=%NATIVE_LIB% org.outerrim.snippad.ui.swt.SnipPad
goto :EOF

:APPEND
set CLASSPATH=%*;%CLASSPATH%
