# ─────────────────────────────────────────────
# Stage 1: Build
# ─────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar solo el pom primero para aprovechar la caché de capas de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn package -DskipTests -B

# ─────────────────────────────────────────────
# Stage 2: Runtime
# eclipse-temurin:17-jre soporta amd64 y arm64 (Apple Silicon)
# la variante -alpine NO tiene imagen arm64
# ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre
WORKDIR /app

# Crear usuario no-root por seguridad (sintaxis Debian/Ubuntu)
RUN groupadd --system compo && useradd --system --gid compo compo

# Copiar el artefacto generado
COPY --from=build /app/target/*.jar app.jar

# Cambiar al usuario no-root
USER compo

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
