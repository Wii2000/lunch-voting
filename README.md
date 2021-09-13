[![Build Status](https://app.travis-ci.com/Wii2000/lunch-voting.svg?branch=master)](https://app.travis-ci.com/Wii2000/lunch-voting)

# Lunch voting

REST API of voting system for deciding where to have lunch.<br>
Used instruments: Spring Boot 2.5, Security, JPA(Hibernate), Maven, Lombok, H2, Swagger/OpenAPI 3.0.

## Initial task

Build a voting system.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
    * If it is before 11:00 we assume that he changed his mind.
    * If it is after 11:00 then it is too late, vote can't be changed
    
Each restaurant provides a new menu each day.

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