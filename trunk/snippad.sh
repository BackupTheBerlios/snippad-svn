#!/bin/bash
LIBRARY_PATH=lib/
SNIPPAD_JAR=dist/snippad.jar

for file in `ls lib`
do
    CLASSPATH=${CLASSPATH}:${LIBRARY_PATH}${file}
done

for file in `ls lib/linux/*.jar`
do
    CLASSPATH=${CLASSPATH}:${file}
done

# Add the main snippad jar
CLASSPATH=$SNIPPAD_JAR:$CLASSPATH

JAVA="$JAVA_HOME"/bin/java
NATIVE_LIB="${LIBRARY_PATH}linux/"

echo $CLASSPATH
echo $NATIVE_LIB
exec $JAVA -cp $CLASSPATH -Djava.library.path=$NATIVE_LIB org.outerrim.snippad.ui.swt.SnipPad
