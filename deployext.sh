#!/bin/sh
set -eu

cd $(dirname $0)

status() {
    echo "$(tput bold)>>> $1 <<<$(tput sgr0)"
}

status "1/1 Building Maven extension and copying to - {maven.home}/lib/ext"
mvn package -DskipTests

cp -v target/maven-build-scanner-1.0.0-SNAPSHOT-jar-with-dependencies.jar $(mvn help:evaluate -Dexpression=maven.home -DforceStdout -q)/lib/ext/


status "Ready. Maven Extension deployed."

echo "To perform a scan, run:"
echo
echo "  env MAVEN_BUILD_SCANNER=1 mvn <goals>"
echo
echo "Open http://localhost:3000 to see scan results."
