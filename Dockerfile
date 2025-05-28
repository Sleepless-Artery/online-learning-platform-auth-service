FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/auth-service-*.jar app.jar
COPY src/main/resources/db/migration/ /app/resources/db/migration/

ENTRYPOINT ["java", "-jar", "app.jar"]