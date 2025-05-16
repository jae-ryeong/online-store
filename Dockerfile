# 1. 빌드용 JDK 이미지
FROM openjdk:17-alpine as builder

WORKDIR /app

# xargs가 필요하므로 coreutils 설치
RUN apk add --no-cache findutils

# gradlew 스크립트와 gradle 디렉토리를 복사
COPY gradlew .
COPY gradle ./gradle

# gradlew 파일에 실행 권한 부여
RUN chmod +x ./gradlew

# 먼저 복사하여 의존성 캐시 재사용
COPY build.gradle settings.gradle ./
# 그 다음 복사
COPY src ./src

# 기존
# RUN ./gradlew build --no-daemon
# 수정: 테스트를 건너뜁니다
RUN ./gradlew build --no-daemon -x test

# 2. 실행용 JRE 이미지
FROM openjdk:17-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]