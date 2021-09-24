# Roombooking Springboot microservice project
Springboot microservice booking system with Docker Jenkins CICD
## AWS host url
#### gateway url
http://www.nobrainer.link:8080/api/v1/app/swagger-ui.html (for reference, swagger not testable)
#### swagger testable url
main app http://www.nobrainer.link:8081/api/v1/app/swagger-ui.html 

dictionary http://www.nobrainer.link:8086/api/v1/dicts/swagger-ui.html 
## Tech Stack
Springboot, Spring data Mongo, Kafka, Braintree payment, Docker, Jenkins
## Jenkins work flow
![image](https://user-images.githubusercontent.com/32782723/133067158-a7a7d992-6264-41d6-9939-36dad1b595cb.png)
#### Jenkins project setting
create parent folder for booking project
create each microservice as a separate build project so that each service can build independently only if the service module has changed.
##### key setting to acheive separate build
in each service build, in Pipeline page, there is Additional Behaviours -> Polling igures commits in cartain paths -> add condition in "Excluded Regions" to exclude other service modules, so that current build will only be triggered if the current module commited changes.

## Kafka 
![img_1.png](img_1.png)






