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

Install the following:

* Java
* Maven

```bash
# Install the Maven extension:
output="$(mvn help:evaluate -Dexpression=maven.home -DforceStdout -q)/lib/ext/maven-build-scanner-jar-with-dependencies.jar"
curl -L https://github.com/intuit/maven-build-scanner/releases/download/v1.0.0/maven-build-scanner-jar-with-dependencies.jar -o $output

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

## Uninstall

```bash
output="$(mvn help:evaluate -Dexpression=maven.home -DforceStdout -q)/lib/ext/maven-build-scanner-jar-with-dependencies.jar"
rm -f $output
```
