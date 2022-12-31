[![CircleCI](https://circleci.com/gh/intuit/maven-build-scanner.svg?style=svg&circle-token=ba2bd2fce7c7779536df8819e5eefc8bc9f05706)](https://circleci.com/gh/intuit/maven-build-scanner)

# Maven Build Scanner

*Know your build - so you can make it faster*

## What Is Maven Build Scanner?

Maven Build Scanner is a tool that hooks into Maven builds and produces reports and charts that breakdown the time Maven spends doing different tasks. These reports provide insights into your builds that can be used to tune it. 

It's been designed to work with large and complex builds; ones with hundreds of modules and plugins.

At Intuit it helped take a 40m build run by hundreds of developers every day and reduce it to 4m - a 10x reduction.
[Read a blog post on the techniques that saved all this time](https://medium.com/@alex_collins/10x-faster-maven-builds-at-intuit-5b7bb60c65e6).

> (30m of engineering time) * (several hundred engineers) * (every working day for 5 years) * (some dollar amount) = (a LOT of money saved)

### Screenshot

![Screenshot](screenshot.png)

### Video

[![Video](video.png)](https://www.youtube.com/watch?v=2tB63Wer-4E)

## How Do I Use It?

Install the following:

* Java
* Maven
* Docker


```bash
# run a mongo database
docker run --rm -p 27017:27017 --name mongo mongo
```

```bash
# run the web server
docker run --rm -p 3000:3000 -e MONGO_DB_HOST=host.docker.internal --name maven-build-scanner-server alexcollinsintuit/maven-build-scanner-server
```

```bash
# Install Maven extension
url='https://github-registry-files.githubusercontent.com/168082607/3640b800-8930-11ed-970b-3117798857db?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20221231%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20221231T173656Z&X-Amz-Expires=300&X-Amz-Signature=603665454ef4d7cbd025a86a0843b3b1810c4bb4a2d632f55697a90edf968724&X-Amz-SignedHeaders=host&actor_id=0&key_id=0&repo_id=168082607&response-content-disposition=filename%3Dmaven-build-scanner-1.0.0-20221231.172545-1-jar-with-dependencies.jar&response-content-type=application%2Foctet-stream'
output="$(mvn help:evaluate -Dexpression=maven.home -DforceStdout -q)/lib/ext/maven-build-scanner-jar-with-dependencies.jar"

curl -v $url -o $output

```

Creating your first scan:

```bash
env MAVEN_BUILD_SCANNER=1 mvn install
```

You can then view your first scan at <http://localhost:3000>. The page will show:

* A timeline of the build, so you can see how effective concurrency is being used.
* A pie chart showing the Maven plugins that took the longest.
* A module-by-module breakdown of tasks.
* Links to reports on previous builds.

To create a scan for another application, do the following:

    cd your-app
    env MAVEN_BUILD_SCANNER=1 mvn install

You should see the following line in the Maven console output:

    Open http://localhost:3000/?projectId=com.intuit:maven-build-scanner&sessionId=60acc519-ff2a-4c06-b79a-2aa23c47c861 to view your Maven build scanner results to view your Maven build scanner results

