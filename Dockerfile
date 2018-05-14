FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} authserver.0.0.1.jar
ADD keyserverstore.keystore ./
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/authserver.0.0.1.jar"]
