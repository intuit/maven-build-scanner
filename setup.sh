#!/bin/sh
set -eu

cd $(dirname $0)

status() {
    echo "$(tput bold)>>> $1 <<<$(tput sgr0)"
}

#status "1/5 Starting local Mongo container"
#[ -e db ] || mkdir db
#[ "$(docker ps -q -f name=build_scan_db)" = '' ] && docker run -d -v $(pwd)/db:/data/db -P -p 27017:27017 --rm --name build_scan_db mongo
status "1/3 Starting local containers (web server mongo db)"
docker-compose up --build -d

status "2/3 Building Maven extension and copying to - maven.home/lib/ext"
mvn package
#-DskipTests

cp -v target/maven-build-scanner-1.0.0-SNAPSHOT-jar-with-dependencies.jar $(mvn help:evaluate -Dexpression=maven.home -DforceStdout -q)/lib/ext/

status "3/3 Creating your first scan"
#env MAVEN_BUILD_SCANNER=1 mvn verify -q -DskipTests

status "Ready"

echo "To perform a scan, run:"
echo
echo "  env MAVEN_BUILD_SCANNER=1 mvn <goals>"
echo
echo "Open http://localhost:3000 to see scan results."
