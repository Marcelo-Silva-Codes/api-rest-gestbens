# ---------- Estágio 1: compilar ----------
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build

# Copia só o que define as dependências, para aproveitar o cache do Docker
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Remove o \r do mvnw (se foi salvo no Windows), dá permissão de execução
# e baixa as dependências
RUN sed -i 's/\r$//' mvnw \
    && chmod +x mvnw \
    && ./mvnw -B --no-transfer-progress dependency:go-offline

COPY src ./src
RUN ./mvnw -B --no-transfer-progress -DskipTests package


# ---------- Estágio 2: executar ----------
FROM eclipse-temurin:21-jre-alpine

# Usuário sem privilégios de root
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app
COPY --from=builder --chown=spring:spring /build/target/*.jar app.jar
USER spring

# Só documentação: a porta real vem do server.port=${PORT:8080}
EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=65.0", "-XX:+UseSerialGC", "-XX:TieredStopAtLevel=1", "-jar", "app.jar"]