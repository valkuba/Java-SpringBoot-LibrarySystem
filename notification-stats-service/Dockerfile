FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -pl notification-stats-service -am -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=builder /app/notification-stats-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
