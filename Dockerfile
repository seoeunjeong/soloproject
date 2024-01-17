#도커를 이용한 실긍 컨테이너 어플리케이션
FROM openjdk:17
ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} ./app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java","-jar","./app.jar"]
