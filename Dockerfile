FROM eclipse-temurin:17

LABEL maintainer="hive.com"

WORKDIR /app

COPY target/hive-app.jar /app/hive-app.jar

ENTRYPOINT ["java", "-jar", "hive-app.jar"]