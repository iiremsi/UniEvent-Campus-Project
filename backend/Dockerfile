# ═══════════════════════════════════════════════════════════════
#  UniEvent Backend — Multi-stage Dockerfile
# ═══════════════════════════════════════════════════════════════
#
#  Neden multi-stage build?
#  → Stage 1 (builder): Maven + JDK ile uygulamayı derler (~800MB)
#    Stage 2 (runtime): Sadece JRE + JAR dosyası (~300MB)
#    Sonuç: Production image boyutu %60+ küçülür.
#    Güvenlik: Build araçları (Maven, kaynak kod) prod image'da YOK.
#
#  Build:  docker build -t unievent-backend .
#  Run:    docker run -p 8080:8080 unievent-backend
# ═══════════════════════════════════════════════════════════════

# ── Stage 1: Build ───────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Önce sadece pom.xml kopyala → dependency cache katmanı
# Kod değiştiğinde dependency'ler tekrar indirilmez (Docker layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Kaynak kodu kopyala ve paketle
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Stage 2: Runtime ─────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

# Güvenlik: Root olmayan kullanıcı ile çalıştır
RUN addgroup -S spring && adduser -S spring -G spring

# JAR dosyasını builder stage'den kopyala
COPY --from=builder /app/target/*.jar app.jar

# Sahipliği spring kullanıcısına ver
RUN chown spring:spring app.jar
USER spring

# Health check — Kubernetes liveness probe'a alternatif
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

# JVM ayarları — container-aware memory management
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", "app.jar"]
