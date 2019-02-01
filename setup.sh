#!/bin/sh
set -eu

cd $(dirname $0)

status() {
    echo "$(tput bold)>>> $1 <<<$(tput sgr0)"
}

status "1/5 Starting local Mongo container"
[ -e db ] || mkdir db
[ "$(docker ps -q -f name=build_scan_db)" = '' ] && docker run -d -v $(pwd)/db:/data/db -P -p 27017:27017 --rm --name build_scan_db mongo


status "2/5 Building Maven extension"
mvn install -q

cp -v target/maven-build-scanner-1.0.0-SNAPSHOT-jar-with-dependencies.jar $(mvn help:evaluate -Dexpression=maven.home -DforceStdout -q)/lib/ext/

cd server

status "3/5 Building web server"
npm install


status "4/5 Starting web server"
npm start &

cd -

status "5/5 Creating your first scan"
env MAVEN_BUILD_SCANNER=1 mvn verify -q -DskipTests

status "Ready"

echo "To perform a scan, run:"
echo
echo "  env MAVEN_BUILD_SCANNER=1 mvn <goals>"
echo
echo "Open http://localhost:3000 to see scan results."
