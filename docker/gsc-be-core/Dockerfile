FROM eclipse-temurin:21-jdk-alpine AS stage

WORKDIR /srv

RUN $JAVA_HOME/bin/jlink \
    --add-modules ALL-MODULE-PATH \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /srv/jdk

FROM gradle:jdk21-alpine AS builder
COPY --from=stage /srv/jdk /srv/jdk
ENV JAVA_HOME=/srv/jdk
ENV PATH="$JAVA_HOME/bin:$PATH"

WORKDIR /app
COPY build.gradle /app/build.gradle
COPY settings.gradle /app/settings.gradle
COPY src /app/src
RUN gradle build -x test --no-daemon

FROM alpine:latest AS runtime
COPY --from=stage /srv/jdk /srv/jdk
ENV JAVA_HOME=/srv/jdk
ENV PATH="$JAVA_HOME/bin:$PATH"

ENV TZ=Asia/Tokyo
COPY --from=builder /app/build/libs/gsc-be-core-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
