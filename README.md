# Roombooking Springboot microservice project
Springboot microservice booking system with Docker Jenkins CICD

#### Swagger API docs:
http://nobrainer.link:8081/api/v1/app/swagger.html
http://nobrainer.link:8084/api/v1/payment/swagger.html
http://nobrainer.link:8085/api/v1/email/swagger.html
http://nobrainer.link:8086/api/v1/dicts/swagger.html
http://nobrainer.link:8087/api/v1/file/swagger.html

## Tech Stack
Springboot, Consul, Spring data Mongo, Kafka, Keycloak, Braintree payment, Docker, Jenkins
## Architect and Jenkins work flow
![image](https://user-images.githubusercontent.com/32782723/135623205-b19740b4-1a7c-4f47-8ada-efad04c1f1dd.png)

#### Jenkins project setting
create parent folder for booking project
create each microservice as a separate build project so that each service can build independently only if the service module has changed.
##### key setting to acheive separate build
in each service build, in Pipeline page, there is Additional Behaviours -> Polling igures commits in cartain paths -> add condition in "Excluded Regions" to exclude other service modules, so that current build will only be triggered if the current module commited changes.

## Kafka and notification process
![image](https://user-images.githubusercontent.com/32782723/135376850-8e6ff356-5431-41ea-82e8-4b5537de8bb7.png)







