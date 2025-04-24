FROM eclipse-temurin:17-jdk-focal as build

ENV APP_HOME /usr/src/app

COPY target/social-0.0.1-SNAPSHOT.jar $APP_HOME/app.jar

WORKDIR $APP_HOME

ENTRYPOINT exec java -jar app.jar