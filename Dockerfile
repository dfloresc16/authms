FROM openjdk:17.0.2

LABEL maintainer="Diego Flores <dfloresc1602@alumno.ipn.mx>"

WORKDIR /app

COPY ./target/authms-0.0.1-SNAPSHOT.jar .

EXPOSE 8081

ENTRYPOINT ["java","-jar","authms-0.0.1-SNAPSHOT.jar"]
