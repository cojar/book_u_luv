FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/bookuluv-0.0.1.jar
COPY ${JAR_FILE} app.jar
EXPOSE 7070
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]