[![CircleCI](https://circleci.com/gh/intuit/maven-build-scanner.svg?style=svg&circle-token=ba2bd2fce7c7779536df8819e5eefc8bc9f05706)](https://circleci.com/gh/intuit/maven-build-scanner)

# Maven Build Scanner

*Know your build - so you can make it faster*

## What Is Maven Build Scanner?

Maven Build Scanner is a tool that hooks into Maven builds and produces reports and charts that breakdown the time Maven spent. These reports provide insights into your builds that can be used to tune it. 

It's been designed to work with large and complex builds; ones with hundreds of modules and plugins.

At Intuit it helped take a 40m build run by hundreds of developers every day and reduce it to 4m - a 10x reduction.

[![Video](https://img.youtube.com/vi/2tB63Wer-4E/0.jpg)](https://www.youtube.com/watch?v=2tB63Wer-4E)

Inspired by [Gradle Build Scan](https://scans.gradle.com/s/h2ily574bqb4g).

## How Do I Use It?

Start by install the following:

* Java 8
* Maven 3
* Docker

On Linux/MacOs run:

    ./setup.sh

On Windows (cmd.exe) run:

    setup.bat

This will:

1. Start up two docker containers (Mongo database and the web server).
2. Create a JAR for the extension and copies it into your local Maven extensions folder (on MacOS this is something like `/usr/local/Cellar/maven/3.5.4/libexec/lib/ext`)
3. Run a quick build scan on itself so you can try out the report.

You can then view your first scan at (http://localhost:3000). The page will show:

* A timeline of the build, so you can see how effective concurrency is being used.
* A pie chart showing the Maven plugins that took the longest.
* A module-by-module breakdown of tasks.
* Links to reports on previous builds.

To create a scan for another application, do the following:

On Linux/MacOs:

    cd your-app
    env MAVEN_BUILD_SCANNER=1 mvn install

On Windows:

    cd your-app
    set MAVEN_BUILD_SCANNER 1 
    mvn install

You should see the following line in the Maven console output:

    Open http://localhost:3000/?projectId=com.intuit:maven-build-scanner&sessionId=60acc519-ff2a-4c06-b79a-2aa23c47c861 to view your Maven build scanner results to view your Maven build scanner results

# License
Maven Build Scanner is released under the Apache 2.0 licenses. It uses [junit]( https://junit.org/junit4/) which is licensed under EPL 1.0.
