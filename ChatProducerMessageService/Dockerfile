# Build a JAR File


FROM maven:3.8-openjdk-17 AS stage1
WORKDIR /home/chatkafka
ADD localjar/ /home/chatkafka/localjar
ADD localpom/ /home/chatkafka/localpom
ADD localRepo/ /home/chatkafka/localRepo

#COPY --from=stage1 /home/app/target/*.jar chataikafkaevent.jar

#RUN mvn clean --settings /home/app/settings/user-settings.xml
#RUN chmod 755 /home/app/
#RUN mvn install:install-file initialize -Dversion=0.0.2-SNAPSHOT -Dpackaging=jar -DartifactId=ChatAiKafkaEvent -DgroupId=com.genaichat -Dpomfile=/home/app/localpom/pom.xml  clean install package 
#RUN mvn -f /home/app/pom.xml install:install-file initialize validate clean package
#RUN mvn -f /home/app/pom.xml package
RUN mvn deploy:deploy-file -Dfile=./localjar/ChatAiKafkaEvent-0.0.2-SNAPSHOT.jar -DgroupId=com.genaichat -DartifactId=ChatAiKafkaEvent -Dversion=0.0.2-SNAPSHOT -Dpackaging=jar -Durl=file:C:/Users/DELL/.m2/repository/ -DrepositoryId=maven-repository -DupdateReleaseInfo=true


FROM maven:3.8-openjdk-17 AS stage2
WORKDIR /home/chatproduce
#COPY . /home/app/localRepo\com\genaichat\ChatAiKafkaEvent\0.0.2-SNAPSHOT\ChatAiKafkaEvent-0.0.2-SNAPSHOT.jar
COPY --from=stage1 /home/chatkafka/localRepo/com/genaichat/ChatAiKafkaEvent/0.0.2-SNAPSHOT/ChatAiKafkaEvent-0.0.2-SNAPSHOT.jar ChatAiKafkaEvent-0.0.2-SNAPSHOT.jar
COPY . /home/chatproduce/
RUN mvn -f /home/chatproduce/pom.xml clean install package



FROM openjdk:17-jdk-slim
#EXPOSE 8010
WORKDIR /home/chatproduce
COPY --from=stage2 /home/chatproduce/target/*.jar chatproducermessageservice.jar
ENTRYPOINT ["sh", "-c", "java -jar /chatproducermessageservice.jar"]