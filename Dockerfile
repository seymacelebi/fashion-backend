# ADIM 1: "Derleme" Aşaması (Maven ve Java 17 kullanarak)
# Bu aşama, projenizi .jar dosyasına dönüştürür
FROM maven:3.9-eclipse-temurin-17 AS build

# Proje dosyalarını kopyalamak için bir çalışma dizini oluştur
WORKDIR /app

# Önce pom.xml'i kopyala (bağımlılıkları önbelleğe almak için)
COPY pom.xml .

# Bağımlılıkları indir
RUN mvn dependency:go-offline

# Tüm proje kaynak kodunu kopyala
COPY src ./src

# Projeyi derle (testleri atlayarak) ve .jar dosyasını oluştur
RUN mvn clean install -DskipTests

# ADIM 2: "Çalıştırma" Aşaması (Sadece Java 17 JRE kullanarak)
# Bu aşama, derlenen .jar'ı alır ve daha küçük bir imajda çalıştırır
FROM eclipse-temurin:17-jre-focal

# Çalışma dizini oluştur
WORKDIR /app

# Derleme aşamasından (build) oluşturulan .jar dosyasını kopyala
COPY --from=build /app/target/fashion-backend-0.0.1-SNAPSHOT.jar ./app.jar

# Render, 8080 portunu otomatik olarak algılar, ancak belirtmek iyidir
EXPOSE 8080

# Konteyner başladığında çalıştırılacak komut
# NOT: Render, Ortam Değişkenlerini (DB_URL, JWT_SECRET_KEY vb.)
# bu komuta otomatik olarak iletecektir.
CMD ["java", "-jar", "app.jar"]