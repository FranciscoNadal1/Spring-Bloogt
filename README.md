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

# How to run :

You need JDK and maven / mysql is optional:

- Git clone the project
- run the command: 
```
mvn spring-boot:run
```

## Properties file: /src/main/resources/application.properties
You can adjust the server.port and the database configuration. 
If you don't have a database installed, uncomment this : 
```
  spring.h2.console.enabled=true
  spring.datasource.url=jdbc:h2:mem:clientesdb
  spring.datasource.username=admin
  spring.datasource.password=sa
  spring.dataSource.driver-class-name=org.h2.Driver
  spring.jpa.hibernate.ddl-auto=create-drop
```
and remove this : 

```
spring.datasource.url=jdbc:mysql://localhost/db_blogTest
spring.datasource.username=root
spring.datasource.password=root
spring.dataSource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```
# Database schema :
![alt text](https://github.com/FranciscoNadal1/Spring-Bloogt/blob/master/documentation/Database%20Diagram.png)

# Api paths (in progress):

## GET:
```
/api/user/getAllUsers
/api/user/getUserByMail/{mail}
/api/user/getUserById/{id}
/api/user/getUserByUsername/{username}
/api/user/getCommentsByUserId/{id}
/api/user/getPostsByUserId/{id}
/api/posts/getAllPosts
/api/posts/getPostById/{id}
/api/category/getAllCategories
/api/category/getCategoryById/{id}
/api/category/getAllPostsByCategoryId
/api/comments/getAllComments
/api/comments/getCommentById/{id}
/api/hashtags/getAllHashtags
/api/hashtags/getHashtagById/{id}
/api/hashtags/getPostsOfHashtagById/{id}
/api/hashtags/getPostsOfHashtagByName/{hashtagName}
```
## POST:
```
/api/user/newUser
/api/category/newCategory
/api/comments/newComment
/api/hashtags/newHashtag
```
## PUT:
```
/api/category/updateCategory/{id}
```
## DELETE:
```
/api/category/deleteCategory/{id}
```
