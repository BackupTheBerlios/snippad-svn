@echo off
set LIBRARY_PATH=lib
set NATIVE_LIB=%LIBRARY_PATH%\windows
set SNIPPAD_JAR=dist\snippad.jar
set CLASSPATH=

for %%A in ("%LIBRARY_PATH%\*.jar") do call :APPEND %%A
for %%A in ("%NATIVE_LIB%\*.jar") do call :APPEND %%A

set CLASSPATH=%SNIPPAD_JAR%;%CLASSPATH%

set JAVA=%JAVA_HOME%\bin\java

echo %CLASSPATH%
%JAVA% -cp %CLASSPATH% -Djava.library.path=%NATIVE_LIB% org.outerrim.snippad.ui.swt.SnipPad
goto :EOF

:APPEND
set CLASSPATH=%*;%CLASSPATH%
