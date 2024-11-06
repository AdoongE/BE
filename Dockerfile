FROM openjdk:17
COPY .env .env
COPY build/libs/seedzip.jar seedzip.jar
ENTRYPOINT ["java", "-jar", "/seedzip.jar"]
