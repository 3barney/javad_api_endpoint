## delivery_service_customer_service
Rest endpoint exposing customer endpoint operations


## Description
The service is responsible of offering various endpoints necessary to perform customer related operations
as shown on swagger documentation.

## Documentation
To access the api documentation, start the application using the next step ```setup```
and navigate to the following endpoint ```http://localhost:8080/swagger-ui.html#```

## Setup
### Dependencies
Libraries used for project
* Java 1.8
* Docker
* Postgresql

### Getting Started
To start the service
* Clone the project from github 
    * ```git clone https://github.com/3barney/javad_api_endpoint```

### Run The Service  
* Navigate to root of project and start the service by executing
    * ```mvn clean compile && mvn clean package && mvn spring-boot:run```
    * ```./mvnw clean compile```
    * ```./mvnw clean package```
    * ```./mvnw spring-boot:run```

## Testing
* To execute tests defined on this service do run
    * ```mvn test```

