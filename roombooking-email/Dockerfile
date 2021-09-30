FROM openjdk:11-alpine3.9
MAINTAINER samuel
ENV TZ Australia/Sydney
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app.jar"]