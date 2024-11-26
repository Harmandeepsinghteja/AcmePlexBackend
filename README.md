# Movie Theater Ticket Reservation App

## Project Overview
The Movie Theater Ticket Reservation App is designed for AcmePlex, a company with multiple theaters, to facilitate ticket reservations for movie screenings. This application supports two types of users: Ordinary Users and Registered Users (RUs). Ordinary users can search for movies, select theaters, view available showtimes, choose seats, make payments, and receive tickets via email. They can cancel tickets up to 72 hours before the show with a 15% administration fee. RUs pay an annual fee but are exempt from cancellation fees and have early access to movie announcements.
## Features
### User Registration and Login:
Ordinary users can register and log in to access the app's features.
RUs have additional privileges such as fee exemptions and early access.
### Movie Search and Selection:
Users can search for movies and select theaters.
View available showtimes and seating arrangements.
### Seat Selection:
Graphical interface for selecting available seats.
Real-time updates on seat availability.
### Payment Processing:
Secure credit card payment gateway.
Email confirmation of ticket purchase and receipt.
### Ticket Cancellation:
Cancel tickets up to 72 hours before the show.
RUs do not incur cancellation fees.
### Admin Features:
Manage user accounts and theater details.
Monitor bookings and cancellations.
### Technologies Used
Frontend: React.js for user interface development.
Backend: Java Spring Boot for server-side logic.
Database: MySQL for data storage and management.
Payment Gateway: Integration with a secure payment processing service.



## Installation Instructions
1. Clone the Repository:
```
git clone https://github.com/Harmandeepsinghteja/AcmePlexBackend.git
cd AcmePlexBackend
```
2. Backend Setup
-> run resources/data.sql file to create and update database
-> Update application.properties with your database credentials.
    ### Configuration
    Before running the application, you need to configure the application.properties file with your specific settings. Below is a template with explanations     for each property:
````
 // Application Name
spring.application.name=server

// Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/movie_theater
spring.datasource.username=root
spring.datasource.password=password

// JPA and Hibernate Settings
// Uncomment the following lines to load dummy data and initialize the database schema
// spring.jpa.defer-datasource-initialization=true
// spring.sql.init.mode=always

// Make this None in Production // TODO
// spring.jpa.hibernate.ddl-auto=update

// Prevent JPA from changing column names
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

// Show SQL Queries in Logs
spring.jpa.show-sql=true

// Security Configuration (Optional)
// spring.security.user.name=admin
// spring.security.user.password=admin

// Hibernate Properties
// The SQL dialect makes Hibernate generate better SQL for the chosen database
// spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect

// Email Configuration for Sending Tickets and Receipts
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=yourMailingAccount@gmail.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

// CORS Configuration for Frontend Integration
spring.mvc.cors.allowed-origins=http://127.0.0.1:5173
````

   
-> Run the backend server:
```
mvn spring-boot:run

```




