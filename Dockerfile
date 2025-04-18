FROM gradle:7.6.2-jdk17-alpine as builder

USER root
WORKDIR /app
COPY . .
RUN gradle clean build --refresh-dependencies -x test

FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
