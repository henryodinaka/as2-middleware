FROM maven:3.6.3-jdk-8-slim AS build

LABEL maintainer="adewolemayowa@gmail.com"
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace

COPY src /workspace/src
RUN mvn -B clean package --file pom.xml

FROM openjdk:14-slim

COPY --from=build /workspace/target/*as2-middleware.jar as2-middleware.jar

VOLUME [ "/data" ]

EXPOSE 9090

CMD ["java","-jar","as2-middleware.jar"]
