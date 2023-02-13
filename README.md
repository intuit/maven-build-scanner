# Maven Build Scanner

*Know your build - so you can make it faster*

## What Is Maven Build Scanner?

Maven Build Scanner is a tool that hooks into Maven builds and produces reports and charts that breakdown the time Maven
spends doing different tasks. These reports provide insights into your builds that can be used to tune it.

It's been designed to work with large and complex builds; ones with hundreds of modules and plugins.

At Intuit it helped take a 40m build run by hundreds of developers every day and reduce it to 4m - a 10x reduction.
[Read a blog post on the techniques that saved all this time](https://medium.com/@alex_collins/10x-faster-maven-builds-at-intuit-5b7bb60c65e6).

> (30m of engineering time) * (several hundred engineers) * (every working day for 5 years) * (some dollar amount) = (a
> LOT of money saved)

### Screenshot

![Screenshot](screenshot.png)

### Video

[![Video](video.png)](https://www.youtube.com/watch?v=2tB63Wer-4E)

## How Do I Use It?

Using the build scanner requires three steps: 

1. Integrating the build scanner as a [maven extension](https://maven.apache.org/guides/mini/guide-using-extensions.html)
2. Running your build
3. Running the server to browse the statistics

### Alternative 1: Install directly

Install the following:

* Java
* Maven

```bash
# Install the Maven extension:
output="$(mvn help:evaluate -Dexpression=maven.home -DforceStdout -q)/lib/ext/maven-build-scanner-jar-with-dependencies.jar"
curl -L https://github.com/intuit/maven-build-scanner/releases/download/v2.0.0/maven-build-scanner-jar-with-dependencies.jar -o $output

```

```bash
# Create your first scan:
mvn install
```

```bash
# Start the server to view your results:
output="$(mvn help:evaluate -Dexpression=maven.home -DforceStdout -q)/lib/ext/maven-build-scanner-jar-with-dependencies.jar"
java -jar $output
```

This will print out a URL to the report. The report will show:

* A timeline of the build, so you can see how effective concurrency is being used.
* A pie chart showing the Maven plugins that took the longest.
* A module-by-module breakdown of tasks.
* Links to reports on previous builds.

#### Uninstall

```bash
output="$(mvn help:evaluate -Dexpression=maven.home -DforceStdout -q)/lib/ext/maven-build-scanner-jar-with-dependencies.jar"
rm -f $output
```

### Alternative 2: Install using Extension mechanism and docker

Install the following:

* Java
* Maven
* Docker

Clone the github repository and run
```bash
mvn install
```

to build the project and install into your local ```.m2``` diretory.

Create a file ```.mvn/extensions.xml``` in the project you want to analyze:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<extensions>
    <extension>
        <groupId>com.intuit</groupId>
        <artifactId>maven-build-scanner</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </extension>
</extensions>
```

```bash
# Create your first scan:
mvn install
```

Use docker to launch the server:

```bash
# Create your first scan:
docker-compose up -d
```

This will print launch the server listening on port `3000. The report will show:

* A timeline of the build, so you can see how effective concurrency is being used.
* A pie chart showing the Maven plugins that took the longest.
* A module-by-module breakdown of tasks.
* Links to reports on previous builds.

#### Uninstall

- Remove the entry in the `extensions.xml`
- Remove the docker container:

```bash
docker compose stop && docker compose rm
```
