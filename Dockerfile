FROM maven:3-jdk-8

WORKDIR /code

ADD pom.xml /code/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

ADD src /code/src
RUN ["mvn", "package"]
RUN ["cp","target/Java_webCrawlers-0.0.1-SNAPSHOT-jar-with-dependencies.jar","/root/Java_webCrawlers.jar"]

ENTRYPOINT ["mvn","exec:java","-D","exec.mainClass=crawlers.crawlers.Slave"]