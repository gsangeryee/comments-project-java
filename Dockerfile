FROM java:8
EXPOSE 8080
ARG JAR_FILE
ADD target/${JAR_FILE} /messageboard-service.jar
ENTRYPOINT ["java", "-jar","/messageboard-service.jar"]