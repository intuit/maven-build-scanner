[![CircleCI](https://circleci.com/gh/intuit/maven-build-scanner.svg?style=svg&circle-token=ba2bd2fce7c7779536df8819e5eefc8bc9f05706)](https://circleci.com/gh/intuit/maven-build-scanner)

# Maven Build Scanner

*Know your build - so you can make it faster*

## What Is Maven Build Scanner?

Maven Build Scanner is a tool that hooks into Maven builds and produces reports and charts that breakdown the time Maven spends doing different tasks. These reports provide insights into your builds that can be used to tune it. 

It's been designed to work with large and complex builds; ones with hundreds of modules and plugins.

At Intuit it helped take a 40m build run by hundreds of developers every day and reduce it to 4m - a 10x reduction.
[Learn more](https://medium.com/@alex_collins/10x-faster-maven-builds-at-intuit-5b7bb60c65e6)

> (30m of engineering time) * (several hundred engineers) * (every working day for 5 years) * (some dollar amount) = (a LOT of money saved)

![Screenshot](screenshot.png)

Watch it in action:

[![Video](video.png)](https://www.youtube.com/watch?v=2tB63Wer-4E)

Inspired by [Gradle Build Scan](https://scans.gradle.com/s/h2ily574bqb4g).

## How Do I Use It?

Start by install the following:

* Java 8
* Maven 3
* Docker
* Node + NPM

Run:

    ./setup.sh

This will:

1. Start up a Mongo database on Docker for storing data.
2. Create a JAR for the extension and copies it into your local Maven extensions folder (on MacOS this is something like `/usr/local/Cellar/maven/3.5.4/libexec/lib/ext`)
3. Build and start the NPM web application for viewing your reports (listening on port 3000).
4. Run a quick build scan on itself so you can try out the report.

You can then view your first scan at (http://localhost:3000). The page will show:

* A timeline of the build, so you can see how effective concurrency is being used.
* A pie chart showing the Maven plugins that took the longest.
* A module-by-module breakdown of tasks.
* Links to reports on previous builds.

To create a scan for another application, do the following:

    cd your-app
    env MAVEN_BUILD_SCANNER=1 mvn install

You should see the following line in the Maven console output:

    Open http://localhost:3000/?projectId=com.intuit:maven-build-scanner&sessionId=60acc519-ff2a-4c06-b79a-2aa23c47c861 to view your Maven build scanner results to view your Maven build scanner results


## Alternative install via docker-compose and maven 

1. Checkout the project from github
2. Run `docker-compose up -d` to build the server and run mongo db and the server
3. Run `mvn install` to build and install the maven plugin locally
4. Configure the maven plugin using [Maven CoreExtensions](https://maven.apache.org/ref/3.6.3/maven-embedder/core-extensions.html) 
   by adding add the following content to `.mvn/extensions.xml`:

```xml
<extensions xmlns="http://maven.apache.org/EXTENSIONS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/EXTENSIONS/1.0.0 http://maven.apache.org/xsd/core-extensions-1.0.0.xsd">
  <extension>
    <groupId>com.intuit</groupId>
    <artifactId>maven-build-scanner</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </extension>
</extensions>
```
Goto your project and run a maven build with profiling:

To create a scan for another application, do the following:

    env MAVEN_BUILD_SCANNER=1 mvn install

# License
Maven Build Scanner is released under the Apache 2.0 licenses. It uses [junit]( https://junit.org/junit4/) which is licensed under EPL 1.0.
