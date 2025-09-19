# Etapa 1: build
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Etapa 2: runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/moneymanager-0.0.1-SNAPSHOT.jar moneymanager-v1.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "moneymanager-v1.jar"]
