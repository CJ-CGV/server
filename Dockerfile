# 1단계: Gradle로 JAR 빌드
FROM gradle:7.6.2-jdk17-alpine AS builder

WORKDIR /app
COPY . .
RUN gradle build -x test

# 2단계: 빌드된 JAR을 실행용 이미지에 복사
FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
