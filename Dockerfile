FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
# 필요한 환경 변수를 ARG로 선언합니다.
ARG DB_HOST
ARG DB_USER
ARG DB_PASSWORD

# ARG를 ENV로 설정하여 컨테이너 환경 변수로 사용하게 합니다.
ENV DB_URL=$DB_URL
ENV DB_USER=$DB_USER
ENV DB_PASS=$DB_PASS

COPY ${JAR_FILE} seedzip.jar
ENTRYPOINT ["java", "-jar", "/seedzip.jar"]
