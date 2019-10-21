FROM openjdk:8-jdk-alpine
ARG timetype=utc
ADD target/app.jar /app.jar
ADD config/application.$timetype.properties /application.properties
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]