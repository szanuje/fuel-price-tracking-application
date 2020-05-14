FROM openjdk:11-jdk-slim
ADD target/fuel-app-0.0.1-SNAPSHOT.jar .
EXPOSE 9090
CMD java -jar -Dspring.profiles.active=mysql fuel-app-0.0.1-SNAPSHOT.jar