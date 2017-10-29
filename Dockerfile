FROM java:8
MAINTAINER mrcfldr@gmail.com
ADD target/task-service-1.0-SNAPSHOT.jar task-service.jar
ENTRYPOINT ["java","-jar","/task-service.jar"]
EXPOSE 8080