FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/task-tracker-scheduler-0.0.1-SNAPSHOT.jar task-tracker-scheduler.jar

ENTRYPOINT ["java", "-jar", "task-tracker-scheduler.jar"]