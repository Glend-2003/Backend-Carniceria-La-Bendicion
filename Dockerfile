FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Instalar Maven
RUN apt-get update && \
    apt-get install -y maven

# Copiamos el archivo pom.xml y todo el proyecto
COPY . .

# Usamos Maven para compilar
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# Exponemos el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se inicie
ENTRYPOINT ["java", "-jar", "app.jar"]