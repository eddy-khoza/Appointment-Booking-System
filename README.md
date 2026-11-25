# Appointment Booking System
Full-stack appointment booking system — Spring Boot + Angular + Docker =_=
A production-grade full-stack demo that lets customers book branch appointments
and receive a simulated confirmation. Built for the Software Engineer II technical evaluation.

**Stack:** Java 11 · Spring Boot 2.7 · Spring Data JPA · H2 · Angular 15 · Docker · nginx

---

## Project Brief

> Create a system (front end and back end) for customers to schedule branch
> appointments and communicates a simulated confirmation of the appointment.

### Implemented features
- Create, list, view, filter, and cancel appointments
- Conflict prevention: same 30-minute slot can't be double-booked
- Field validation (name, email, future date)
- Simulated confirmation email (logged on the backend console)
- Structured error responses with field-level details
- Swagger UI for API exploration
- Responsive SPA (Book · Appointments · Detail)

---

## Architecture
Angular 15 SPA ──HTTP──> Spring Boot REST API ──JPA──> H2 (in-memory)
│
└─> EmailService (simulated, logs only)


---

## Quick Start — Docker (recommended)

'''bash
git clone https://github.com/eddy-khoza/Appointment-Booking-System.git
cd Appointment Booking System
docker-compose up --build