# Email Sending in Spring Boot

This project is a simple Email Verification System developed using Spring Boot.

## Features

- Send verification email to users
- Email verification using token/link
- Spring Boot backend
- SMTP email configuration
- REST API support
- Maven project structure

## Technologies Used

- Java
- Spring Boot
- Spring Web
- Spring Mail
- Maven
- Lombok

## Project Structure

src/
 ├── main/
 │ ├── java/
 │ └── resources/
 └── test/

## Configuration

Configure your email in `application.properties`

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
Run Project
mvn spring-boot:run
API Example
Send Verification Email
POST /sendMail
Author

Developed by Vinod


Then push again:

```bash id="7n3rga"
git add README.md
git commit -m "Added README file"
git push
