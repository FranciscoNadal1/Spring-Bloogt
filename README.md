# Spring-Bloogt
WIP - Almost finished first version.

## ¿What is this?
It's a collection of Rest APIs that give all functionalities of a blog-like webpage.
With this, you can build a fully functional blog, keeping all the database calls separated, on a different server or a different application. 
You could also expose the Rest APIs to a middleware like mulesoft or oracle service bus and then connect the frontend to that.
Since you have all the Rest APIs already built, it's very easy to build a mobile application for the website.

It also has a fully functional blog embbedded, so you can just work with that.

It's Spring Boot, so you don't even need to install a server like Tomcat or Apache, just having JDK and maven, you can start a server.

This can be very useful for building a portfolio, for prototyping or just for learning, since the database and the rest calls are already made, you can focus on the design, the frontend, the middleware...
This is NOT intended to be used in a production environtment as it is, at least not for now. It's on a very early stage, it doesn't support login(yet) and it doesn't have security.

# How to run on a docker:
With docker installed, just type:
```
docker-compose up
```
This docker includes a mysql server and the deployable app.

# How to run without docker:
You need JDK, maven and mysql:

- Git clone the project
- On the folder of the app, run the command: 
```
mvn spring-boot:run
```

## Properties file: /src/main/resources/application.properties
You can adjust the server.port and the database configuration. This app was developed on mysql

# Database schema :
![alt text](https://github.com/FranciscoNadal1/Spring-Bloogt/blob/master/documentation/Database%20Diagram.png)

# Api paths (in progress):

A postman collection is included on the repository:

![alt text](https://github.com/FranciscoNadal1/Spring-Bloogt/blob/master/documentation/Postman%20Folders.png)

[Postman collection](https://github.com/FranciscoNadal1/Spring-Bloogt/blob/master/documentation/Blog%20Rest%20Api.postman_collection.json)


