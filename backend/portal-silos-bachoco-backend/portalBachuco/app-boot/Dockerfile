# --- ETAPA 1: CONSTRUCTOR (Builder) ---
# Usa una imagen de Maven con Java 21 (Temurin) para compilar el proyecto Spring Boot
FROM maven:3.9-eclipse-temurin-21 AS builder

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo de configuración del proyecto (pom.xml)
# Esto permite que Maven descargue dependencias y aproveche la caché de Docker
COPY pom.xml .

# Descarga las dependencias del proyecto (opcional pero recomendado para caching)
RUN mvn dependency:go-offline

# Copia el código fuente (incluyendo los estáticos de Angular ya depositados en src/main/resources/static)
COPY src ./src

# Compila y empaqueta el proyecto Spring Boot en un archivo WAR.
# El archivo WAR contendrá el backend y los estáticos del frontend.
RUN mvn clean package -DskipTests

# ----------------------------------------------------------------------------------------------------

# --- ETAPA 2: IMAGEN FINAL DE DESPLIEGUE (Runtime en Tomcat) ---
# Usamos una imagen base de Tomcat que ya incluye un JRE 21 (por ejemplo, Eclipse Temurin o una base Tomcat)
# Nota: Asumo que el puerto por defecto de Tomcat es 8080.
FROM tomcat:10.1-jdk21-temurin-jammy

# Elimina la aplicación de ejemplo predeterminada de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia el archivo WAR final desde la etapa del constructor (Builder)
# El archivo WAR se copia con el nombre ROOT.war para que se despliegue en el contexto raíz (/) de Tomcat.
# Reemplaza 'nombre-del-archivo-war-generado.war' con el nombre real de tu WAR
# que está en tu pom.xml (e.g., mi-aplicacion-1.0.0.war)
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expone el puerto por defecto de Tomcat
EXPOSE 8080

# El comando de inicio predeterminado de Tomcat ya está en la imagen base.
# No es necesario agregar un ENTRYPOINT o CMD adicional si quieres usar el comportamiento por defecto.
