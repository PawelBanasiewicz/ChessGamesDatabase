![presentation](images/logo.png)
ChessGamesDatabase is a web application designed for managing chess games, players, and openings. 
It features a role-based access control system that accommodates different user levels, 
including anonymous users, regular users, moderators, and admins, each with distinct permissions.

![presentation](images/presentation.gif)

## Features
- Anonymous Users: Can browse the database of chess games, players, and openings.
- Logged-in Users: Can save games, players, and openings to their favorites.
- Moderators: Have the ability to add, delete, and modify games, players, and openings.
- Admins: Manage user accounts, including creating, deleting, and modifying user roles.

## Technologies Used
- Java 17: The programming language used for building the application.
- Spring Boot: The framework used for developing the backend services.
- Spring Security: Provides authentication and authorization features for user management.
- JPA/Hibernate: Used for data persistence and database interaction.
- Thymeleaf: A server-side template engine for dynamic HTML rendering.
- MySQL: The relational database used for storing chess games, players, and user data.
- HTML & CSS: For structuring and styling the frontend user interface.
- Gradle: The build automation tool used for project management.

## Prerequisites
- MySQL server:
```
localhost:3306/chess_games
```
- Created database with user and password:

``` sql
CREATE USER 'chess_admin'@'localhost' IDENTIFIED BY 'chess';
CREATE DATABASE chess_games;
GRANT ALL PRIVILEGES ON chess_games.* TO 'chess_admin'@'localhost';
```
