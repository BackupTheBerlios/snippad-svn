#!/bin/bash
LIBRARY_PATH=lib/
NATIVE_LIB=${LIBRARY_PATH}/linux/
SNIPPAD_JAR=dist/snippad.jar

for file in `ls ${LIBRARY_PATH}/*.jar`
do
    CLASSPATH=${CLASSPATH}:${file}
done

for file in `ls ${NATIVE_LIB}/*.jar`
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
