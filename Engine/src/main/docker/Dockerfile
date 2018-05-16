FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD engine-0.0.1.jar app.jar
RUN mkdir -p archive
COPY archive/* archive/
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
EXPOSE 8000
ENTRYPOINT exec java $JAVA_OPTS -server -XX:+AlwaysPreTouch -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -Djava.security.egd=file:/dev/./urandom -jar /app.jar
