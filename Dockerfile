FROM openjdk:18
LABEL desc="This is a currency rate save/display application"
COPY target/currencyApp-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
EXPOSE 7070
ENTRYPOINT ["java","-jar","/app.jar"]