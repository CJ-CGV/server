# OpenJDK 17 기반 이미지 사용
FROM bellsoft/liberica-openjdk-alpine:17

# JAR 파일의 위치를 빌드 아규먼트로 지정
ARG JAR_FILE=build/libs/*.jar

# JAR 파일을 컨테이너에 복사
COPY ${JAR_FILE} app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]