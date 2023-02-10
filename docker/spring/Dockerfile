FROM alpine/git as git

ARG URL_GIT
ENV URL_GIT $URL_GIT
WORKDIR /app

RUN git clone $URL_GIT

FROM maven:alpine as build

WORKDIR /app

COPY --from=git /app/trialTaskKameleoon /app
RUN --mount=type=cache,target=/root/.m2 mvn clean package -Dmaven.test.skip


FROM amazoncorretto:8 as run
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

ENTRYPOINT ["java","-jar","app.jar"]