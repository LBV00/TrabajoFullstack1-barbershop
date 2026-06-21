FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java","-jar","app.jar"]