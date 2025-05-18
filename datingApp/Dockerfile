# Build stage
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle clean build --no-daemon

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/datingApp.jar ./app.jar

# Set timezone to Asia/Kolkata
ENV TZ=Asia/Kolkata
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Set JVM options
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:InitiatingHeapOccupancyPercent=45 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod"

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
