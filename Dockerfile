# 1. 빌드용 JDK 이미지
FROM openjdk:17 as builder

WORKDIR /app

# gradlew 스크립트와 gradle 디렉토리를 복사
COPY gradlew .
COPY gradle ./gradle

# 의존성 캐시 최적화
COPY build.gradle settings.gradle ./
COPY src ./src

# 빌드
RUN ./gradlew build --no-daemon

# 2. 실행용 JRE 이미지
FROM openjdk:17

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]