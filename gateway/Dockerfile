FROM openjdk:8-jdk-alpine
COPY target/gateway-1.0-SNAPSHOT.jar gateway-1.0.jar
ENTRYPOINT ["java","-jar","gateway-1.0.jar"]
EXPOSE 8080