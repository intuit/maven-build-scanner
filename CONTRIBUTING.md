# Contribution Guidelines

Before creating a pull request:

1. Successfully built and run all unit tests.
2. Verified that the you can run using the server application.
3. Make sure that the `./setup.sh` script works.

## Running Locally

Install:

- Java
- Maven
- NPM

Start mongo:

```bash
docker compose up mongo
```

Start UI:

```bash
cd server
npm install
npm start
```

Install extension:

```bash
mvn dependency:go-offline
mvn clean package
cp -v target/maven-build-scanner-jar-with-dependencies.jar $(mvn help:evaluate -Dexpression=maven.home -DforceStdout -q)/lib/ext/
```


Open UI on <http://localhost:3000>.