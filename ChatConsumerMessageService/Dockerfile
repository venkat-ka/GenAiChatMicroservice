# Build a JAR File
FROM maven:3.8-openjdk-17 AS stage1
WORKDIR /home/app
COPY . /home/app/
RUN mvn -f /home/app/pom.xml clean package

# Create an Image
FROM openjdk:17-jdk-slim
#EXPOSE 8082
COPY --from=stage1 /home/app/target/*.jar chatconsumermessageservice.jar
ENTRYPOINT ["sh", "-c", "java -jar /chatconsumermessageservice.jar"]