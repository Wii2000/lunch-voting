[![Build Status](https://app.travis-ci.com/Wii2000/lunch-voting.svg?branch=master)](https://app.travis-ci.com/Wii2000/lunch-voting)

# Lunch voting

REST API of voting system for deciding where to have lunch.<br>
Used instruments: Spring Boot 2.5, Security, JPA(Hibernate), Maven, Lombok, H2, Swagger/OpenAPI 3.0.

## Running lunch-voting locally

### With maven command line
```
git clone https://github.com/Wii2000/lunch-voting.git
cd lunch-voting
mvn spring-boot:run
```
### Default credentials
Type|Login|Password
------|------|------
Admin|`admin@mail.com`|`admin`
Regular user|`user@mail.com`|`password`


## Swagger REST API documentation presented here (after application start):
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)