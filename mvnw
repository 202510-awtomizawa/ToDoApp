#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper script
# ----------------------------------------------------------------------------

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(cd "$(dirname "$0")" && pwd)}"
WRAPPER_DIR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper"
WRAPPER_JAR="$WRAPPER_DIR/maven-wrapper.jar"

JAVA_CMD="java"
if [ -n "$JAVA_HOME" ]; then
  JAVA_CMD="$JAVA_HOME/bin/java"
fi

exec "$JAVA_CMD" -classpath "$WRAPPER_JAR" -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" org.apache.maven.wrapper.MavenWrapperMain "$@"
