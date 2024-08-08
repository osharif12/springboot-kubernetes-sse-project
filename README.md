# Springboot-Kubernetes-SSE-Project

## About the Project
This project has two modules representing two services, one named Broadcaster and the other named ViewBroadcaster.
Broadcaster will start a serversocket and wait for socket connections on port 8081. For every connection it will
generate a random number from 1-10, sleep for that random number interval, then send the message to ViewBroadcaster
service. ViewBroadcaster is just a spring boot service that will allow users to type in a url and see these hello world
broadcasts coming in live. 

To allow for REST endpoints to provide continuous updates and live notifications, I used Java
server-send-events. When the ViewBroadcaster service receives incoming hello world messages, I use the SseEmitter class
to push live updates to the url as the messages are coming in. To host this on minikube, I needed to create Dockerfiles
to containerize my services. I also needed to make yaml files for deployments and services.

These services will be deployed on kubernetes pods with replicas to handle for many requests. The technologies being used include Spring Boot, Maven, Docker, Kubernetes, and Java Server Side Events (SSE).

## Information about Server Side Events
SSE's are a tool used for sending real time notifications and updates to web applications. These events are pushed from server to client via HTTP. SSE's are well-suited for scenarios such as a live news feed, a sports game result updates, or a financial dashboard that requires frequent data refresh. 

## Prerequisites to run the application
- Install minikube, docker, kubectl, and maven

## Directions to run application
- clone the project into your repository
- cd into topmost project directory and run 'mvn clean install package' directory (will install jar files inside target directory)
- start minikube with the following command 'minikube start'
- Run the following command to allow for the building of docker images inside minikube: eval $(minikube docker-env)
- cd into the module folder Broadcaster and run: docker build -t broadcaster:latest .
- cd into the module folder ViewBroadcaster and run: docker build -t view-broadcasts:latest .
- you can verify the docker images are there with command: docker images
- cd into the folder Broadcaster and run the following to spin up deployments and pods: kubectl apply -f broadcaster-deployment.yaml
- run the following to spin up service: kubectl apply -f broadcaster-service.yaml
- cd into the folder ViewBroadcaster and run the following to spin up deployments and pods: kubectl apply -f broadcasts-deployment.yaml
- run the following to spin up service: kubectl apply -f broadcasts-service.yaml

### Verify two services, two deployments, and two pods are running with the following commands:
- kubectl get services
- kubectl get deployments
- kubectl get pods

### Access the Kubernetes cluster
- Run this command to return a url to connect to the minikube service: minikube service broadcasts-service --url
- Copy the entire url (should like something like this but with different numbers: http://127.0.0.1:55362)
- Take the url you copied, append server-events/view-broadcasts, then hit enter and enjoy

### Resources
https://medium.com/@AlexanderObregon/how-to-implement-server-sent-events-sse-in-spring-boot-620024272ccb

https://www.baeldung.com/java-executor-service-tutorial

https://medium.com/globant/server-sent-events-in-spring-boot-6a2c512a5475
