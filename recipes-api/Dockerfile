FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle shadowJar --no-daemon

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/*-all.jar app.jar
COPY --from=build /app/images ./images
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 