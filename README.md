# YKB Employee Leave Requests Case

YKB employee leave requests project implementation with Spring Boot.
Swagger and auth paths are set to public.
Users can send requests to other services with the token they received after logging in.
The login service will return the **JWT Token** response to the user.
The user will add the token it received from this response to the **Authorization** key in the header of each request he throws.

Basically, there are two different user roles, **ADMIN** and **USER**.
Role-based restrictions have been implemented in accessing API services.
Messages are displayed to users in English (Default) and Turkish.
In order to change the language in the messages returned in the responses, the **Accept-Language** key and values such as en, tr should be added to the header value in requests.


### Steps
1. Go to project root folder
2. docker-compose --env-file .env  up -d
3. mvn clean install
4. Run Project


### Database
MySQL RDBMS was used in the project.
After successfully running the docker-compose command, can be connected to the db with the information given below;
`Url: localhost:3307
User: root
Pass: 123456`


### Initial Users
Admin:
`Email: admin@admin.com
    Pass: admin
    Role: ADMIN`

User:
`Email: user@user.com
    Pass: user@
    Role: USER`

* Users with ADMIN role can add new users to the application.
* SWAGGER UI URL: http://localhost:8080/swagger-ui/
* API Docs URL: http://localhost:8080/v2/api-docs
* Postman collection on the project root folder: case.postman_collection.json

Request Header -> Accept-Language: [en (default), tr]