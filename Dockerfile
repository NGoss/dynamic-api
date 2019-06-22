FROM openjdk:8-jdk-alpine

WORKDIR /source

COPY ./* ./

RUN ./gradlew build

EXPOSE 8080

CMD java -jar ./build/libs/dynamicapi-0.0.1-SNAPSHOT.jar