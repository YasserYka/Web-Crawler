# Distributed Web Crawlers 
This project is research-based, meaning what I've implemented so far is what I got from reading books or online papers. I don't know if what I'm doig is correct or not but what I know is that I like what I'm doing and I learned a lot from it.

![Getting Started](sd.png)

# Usage
First, run the docker compose with this command
```
$ docker-compose up -d
```
Then with maven installed, run
```
$ mvn exec:java@master
```
or with Dockerfile, run
```
$ docker build -f Dockerfile_Master -t master . && docker run '--network=host' -it master
```
Then in another terminal window, run
```
$ mvn exec:java@slave
```
or with Dockerfile, run
```
$ docker build -f Dockerfile_Slave -t slave . && docker run '--network=host' -it slave
```

# For collaborators
Feel free to participate, propose new features or fix some mistakes.
