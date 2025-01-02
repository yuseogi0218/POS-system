# Build 단계: Java 21 JDK를 사용하는 Gradle 이미지
FROM gradle:jdk21 AS builder

WORKDIR /app

# 프로젝트 소스 복사
COPY . .

# Gradle 빌드 실행 (테스트 생략)
RUN gradle :batch-server:build -x test --no-daemon

# Run 단계: Java 21 JRE를 사용하는 경량 이미지
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 빌드 결과 JAR 파일 복사
COPY --from=builder /app/batch-server/build/libs/*.jar app.jar

# Spring Boot 설정 파일 복사
COPY --from=builder /app/batch-server/src/main/resources/application.yml /app/application.yml
COPY --from=builder /app/batch-server/src/main/resources/application-prod.yml /app/application-prod.yml

# 컨테이너 실행 시 Spring Boot 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/application.yml"]
