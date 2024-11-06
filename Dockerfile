FROM openjdk:17
COPY build/libs/seedzip.jar seedzip.jar
ENTRYPOINT ["java", "-jar", "/seedzip.jar"]
