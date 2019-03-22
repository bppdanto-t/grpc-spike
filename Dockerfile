FROM adoptopenjdk/openjdk8:latest
VOLUME /tmp
ARG JAR_FILE=build/libs/grpc-0.0.1-SNAPSHOT.jar
WORKDIR /opt/springbootapp
COPY ${JAR_FILE} /opt/springbootapp/app.jar
# add curl for healthcheck
RUN apt-get update && apt-get install curl
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
EXPOSE 8080
EXPOSE 6565