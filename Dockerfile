FROM openjdk:14-slim
LABEL maintainer="adewolemayowa@gmail.com"
RUN  apt install awscli
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY target /workspace/target
#RUN cp /workspace/target/as2-middleware.jar as2-middleware.jar
#COPY /workspace/target/as2-middleware.jar as2-middleware.jar
COPY /target/as2-middleware.jar as2-middleware.jar
VOLUME [ "/data" ]
EXPOSE 9090
CMD ["java","-Dspring.profiles.active=staging","-jar","as2-middleware.jar"]