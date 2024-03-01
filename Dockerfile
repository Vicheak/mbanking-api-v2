FROM openjdk:17-alpine

MAINTAINER vicheakbank

WORKDIR /app

COPY build/libs/mbanking-api-v2-1.0.0.jar /app/mbanking-api-v2-1.0.0.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "mbanking-api-v2-1.0.0.jar"]