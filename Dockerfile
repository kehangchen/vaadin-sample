FROM openjdk:8-jdk
VOLUME /tmp
ADD target/*.war app.war
RUN sh -c 'touch /app.war'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.war"]
EXPOSE 8081