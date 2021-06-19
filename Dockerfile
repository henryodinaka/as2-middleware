FROM openjdk:14-slim
LABEL maintainer="adewolemayowa@gmail.com"
RUN mkdir -p /workspace
WORKDIR /workspace
COPY target /workspace/target
RUN cp /workspace/target/as2-middleware.jar as2-middleware.jar
#COPY /workspace/target/as2-middleware.jar as2-middleware.jar
VOLUME [ "/data" ]
EXPOSE 9090
CMD ["java","-jar","as2-middleware.jar"]