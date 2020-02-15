FROM openjdk:12-alpine

COPY target/Java-webCrawlers*.jar /crawler.jar

CMD["java", "-jar", "/crawler.jar"]