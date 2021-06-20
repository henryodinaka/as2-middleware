FROM openjdk:14-slim
LABEL maintainer="adewolemayowa@gmail.com"
RUN apt-get update && \
    apt-get install -y \
        python3 \
        python3-pip \
        python3-setuptools \
        groff \
        less \
    && pip3 install --upgrade pip \
    && apt-get clean

RUN pip3 --no-cache-dir install --upgrade awscli
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY target /workspace/target
#RUN cp /workspace/target/as2-middleware.jar as2-middleware.jar
#COPY /workspace/target/as2-middleware.jar as2-middleware.jar
COPY /target/as2-middleware.jar as2-middleware.jar
VOLUME [ "/data" ]
EXPOSE 9090
CMD ["java","-Dspring.profiles.active=staging","-jar","as2-middleware.jar"]