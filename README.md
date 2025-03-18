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

## Install Stripe CLI for local usage

[Get started with the Stripe CLI](https://docs.stripe.com/stripe-cli?install-method=homebrew)

## Running the Project Locally

### Start the application
```sh
docker-compose up --build -d
```

### Start the Stripe webhook listener
```sh
stripe listen --forward-to localhost:8080/api/payments/stripe-webhook
```

### Swagger URL
```sh
http://localhost:8080/swagger-ui/index.html
```