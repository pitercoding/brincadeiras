# Etapa 1 — build com Maven
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Instala Maven
RUN apt-get update && apt-get install -y maven

# Copia arquivos
COPY pom.xml .
COPY src ./src

# Build
RUN mvn -B clean package -DskipTests

# Etapa 2 — imagem final
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]