#!/usr/bin/env bash

set -e

JAVA_HOME="/c/dev/tools/Java/jdk-21.0.7"
MAVEN_HOME="/c/dev/tools/Maven/apache-maven-3.9.6"

export JAVA_HOME
export MAVEN_HOME
export PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH"

echo "Using JAVA_HOME=$JAVA_HOME"
echo "Using Maven from $MAVEN_HOME"
java -version
mvn -v

mvn clean install
