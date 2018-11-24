<img src="https://www.bsu.by/Cache/Page/653933.jpg" align="right"
     title="Size Limit logo by Anton Lovchikov" style="width: 300; height:125;"/>
 <h1>Chatter web-application developed by Paul Drozd for course project at Belarussian State University</h1>

 ## Description
 
Project designed and implemented for master's degree course "Enterprise Software Engineering" at Belarussian State University by Paul Drozd (drozdps@gmail.com, skype: paul_drozd). I used following literature while working on the project:
* https://www.apress.com/br/book/9781484228074 - common information about Spring framework
* https://www.apress.com/br/book/9781484229842 - description of Java Big Data techinques I used in the project

 ## Features
 
* User registration (via the sign up page /registration).
* Separation of authorities (admin and usual users). Only admins can create chat rooms. 
* 2 supported languages: English and Russian. A user can switch between languages on any page.
* Chat rooms functionality: a user can join any chat room he(she) likes. A user can see other users in a chat room. 
* Public (broadcast) messages: a user can send a message in a chat room so any users of the same room will be able to read the message.
* Private message: a user can select another user and send private messages to him, so other users will not be able to read their conversation.
* Long-term message history
* Distributed database Cassandra. I used Apache Cassandra database as a long-term vault for user messages. It supports Big Data technologies and works extremely effectively in this project. The database restores previous messages when a user logs into the system:
```
CREATE TABLE IF NOT EXISTS message (username text, date timestamp, fromUser text,  roomId text, toUser text, text text, PRIMARY KEY ((username, roomId), date)) WITH CLUSTERING ORDER BY (date ASC)
```
* In-memory key-value database Redis, which I used to store rooms with users so a User can quickly join or leave any group.
* Sophisticated security with Spring Security Framework. All passwords are encrypted
* MySQL was used as the main database storing following tables:
![MySQL scheme](https://github.com/drozdps/Chat/blob/master/screens/schema.png)

 ## Technologies used
 
* **[Java](https://www.oracle.com/technetwork/java/javase/documentation/index.html)**
* **[Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)**
* **[Spring Boot](http://spring.io/projects/spring-boot)**
* **[Sockjs](https://github.com/sockjs/sockjs-client)**
* **[Redis](https://redis.io/)**
* **[Cassandra](http://cassandra.apache.org/)**


 

## Screenshots

Registration page:

![Registration page](https://github.com/drozdps/Chat/blob/master/screens/registration.png)

Login page:

![Login page](https://github.com/drozdps/Chat/blob/master/screens/login.png)
