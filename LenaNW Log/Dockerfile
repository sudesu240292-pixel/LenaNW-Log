FROM maven:3.8.6-openjdk-8

WORKDIR /app

# Proje dosyalarını kopyala
COPY pom.xml .
COPY src ./src

# Maven build
RUN mvn clean package

# JAR dosyasını çıktı klasörüne kopyala
RUN mkdir -p /output
RUN cp target/LenaNW-Log-1.0.0.jar /output/

# Çıktı klasörünü volume olarak bağla
VOLUME /output

CMD ["echo", "Build completed! JAR file is in /output/"] 