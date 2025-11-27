# Etapa 1 — build com Maven
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Instala Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copia arquivos de build
COPY pom.xml .
COPY src ./src

# Build do projeto, ignorando testes
RUN mvn clean package -DskipTests

# Etapa 2 — imagem final
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia o JAR gerado da etapa de build
COPY --from=build /app/target/*.jar ./app.jar

# Porta que o aplicativo vai expor
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "./app.jar"]