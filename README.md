# Springboot-Kubernetes-SSE-Project
 Creating two services, one for broadcasting hello messages in a random interval from 1-10 and another service to receive these broadcasts and display them coming in live on a url which can be accessed by a client. These services will be deployed on kubernetes pods with replicas to handle for many requests. The technologies being used include Spring Boot, Maven, Docker, Kubernetes, and Java Server Side Events (SSE).



### Information about Server Side Events
SSE's are a tool used for sending real time notifications and updates to web applications. These events are pushed from server to client via HTTP. SSE's are well-suited for scenarios such as a live news feed, a sports game result updates, or a financial dashboard that requires frequent data refresh. 
