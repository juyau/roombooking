FROM azul/zulu-openjdk-alpine:11.0.11-jre
MAINTAINER samuel
ENV TZ Australia/Sydney
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]