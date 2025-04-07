# Cinema Management Platform

A modern platform for managing a cinema, booking tickets, and handling payments.

## Features
- **Browse movie listings** – View upcoming and currently running movies.
- **Interactive seat selection** – Choose seats on a visual hall layout.
- **Secure online payments** – Pay for tickets via Stripe.
- **Instant ticket generation** – Receive a PDF ticket with a QR code after payment.
- **Admin panel** – Manage movies, halls, and sessions.

## Tech Stack
- **Docker** – Simplifies deployment across different platforms.
- **React + TypeScript** – Provides an interactive UI for seat selection and booking.
- **Spring Boot 3 + Java 21** – Handles business logic, session management, and payments.
- **Stripe API** – Enables seamless online transactions.
- **Swagger** – API documentation for easy integration and testing.

## Running the Project Locally

### Set up credentials

Firstly, property files should be updated with necessary credentials:
- Update stripe and email configs in **application.properties** file.
- Change **STRIPE_API_KEY** in the **docker-compose.yaml** file.

### Start the application
```sh
docker-compose up --build -d
```

### Access the application via URL
```sh
http://localhost:5173/
```

### Swagger URL
```sh
http://localhost:8080/swagger-ui/index.html
```