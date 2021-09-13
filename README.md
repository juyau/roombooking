# Roombooking Springboot microservice project
Springboot microservice booking system with Docker Jenkins CICD
## Tech Stack
Springboot SpingCloud
## Jenkins work flow
![image](https://user-images.githubusercontent.com/32782723/133067158-a7a7d992-6264-41d6-9939-36dad1b595cb.png)
#### Jenkins project setting
create parent folder for booking project
create each microservice as a separate build project so that each service can build independently only if the service module has changed.
##### key setting to acheive separate build
in each service build, in Pipeline page, there is Additional Behaviours -> Polling igures commits in cartain paths -> add condition in "Excluded Regions" to exclude other service modules, so that current build will only be triggered if the current module commited changes.

