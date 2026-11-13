# HospitalManagmentSystemWithBilling

MediCare hospital management and billing application built with Spring Boot, Thymeleaf, and MySQL.

## Requirements

- Java 21
- Maven
- MySQL

## Configure and run

Create the `hospital_db` database and its tables, then set the database connection variables:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/hospital_db?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-local-mysql-password"
mvn spring-boot:run
```

The application also accepts `PORT` for the HTTP port. The local SQL dump is intentionally not included because it contains seeded records; use a clean schema when setting up a new environment.

## Deployment

This is a server-rendered Spring Boot application and cannot be deployed directly to Vercel. Deploy it on a Java-capable application host and configure `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and optionally `PORT` there.
