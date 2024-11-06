FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} seedzip.jar
ENTRYPOINT ["java", "-jar", "/seedzip.jar"]
