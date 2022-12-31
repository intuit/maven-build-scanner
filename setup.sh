#!/bin/sh
set -eu

cd $(dirname $0)

status() {
    echo "$(tput bold)>>> $1 <<<$(tput sgr0)"
}

status "1/2 Starting containers"
docker compose up -d

status "2/3 Building Maven extension"
mvn clean install -q

cp -v target/maven-build-scanner-jar-with-dependencies.jar $(mvn help:evaluate -Dexpression=maven.home -DforceStdout -q)/lib/ext/

status "3/3 Creating your first scan"
env MAVEN_BUILD_SCANNER=1 mvn verify -q -DskipTests

status "Ready"

echo "To perform a scan, run:"
echo
echo "  env MAVEN_BUILD_SCANNER=1 mvn <goals>"
echo
echo "Open http://localhost:3000 to see scan results."
