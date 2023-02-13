FROM eclipse-temurin:11.0.17_8-jre
COPY target/*-jar-with-dependencies.jar server.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "server.jar"]
