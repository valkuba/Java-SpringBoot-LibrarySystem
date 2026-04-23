# PA165-project-team3-group02 - Library Management System

## Project Description
The goal of this project is to create the information system that enables a virtual library
to manage their resources e.g. books, services. The services are represented by the ability for the
registered members to reserve a book and then borrow it for pre-defined period of time.
It also handles late return fines and provides statistical reports for librarians.
Project is based on the Spring Boot framework.

---

## Team Members
| Name | Service | Role |
|------|---------|------|
| Stanislav Krč | Member service | Developer |
| Milan Takáč | Lending service | Developer |
| Jakub Valeš | Notification and Statistics Service | Developer |
| Adam Pap | Book service | Team Leader |

---

## Executable Modules
The project consists of the following independently runnable Spring Boot modules:

| Module | Port | Swagger UI |
|--------|------|------------|
| `book-service` | 8080 | http://localhost:8080/swagger-ui/index.html |
| `lending-service` | 8081 | http://localhost:8081/swagger-ui/index.html |
| `member-service` | 8082 | http://localhost:8082/swagger-ui/index.html |
| `notification-stats-service` | 8083 | http://localhost:8083/swagger-ui/index.html |

---

## Build & Run

### Requirements
- Java 25
- Maven
- Podman + podman-compose or Docker + docker-compose

### Build all modules
```bash
mvn clean install
```

### Run a specific service
```bash
cd lending-service
mvn spring-boot:run
```

### Run entire system with Podman or Docker
```bash
# Start all services using Podman Compose
podman machine init
podman machine start
podman-compose up --build

# Stop all services
podman-compose down
```

Each service exposes its REST API documentation via Swagger UI at the URL listed above.

---

## Architecture
The project is equally divided into 4 microservices, each owned by one team member.

### 1. Book Service (port 8080)
- Managing complete book inventory and catalogue
- Management of book metadata (titles, authors, ISBN, genres)
- Search and browsing API for both anonymous users and registered members
- Managing number of available copies

### 2. Member Service (port 8082)
- Managing member accounts (username, email, password)
- Sign in / sign up (authentication)

### 3. Lending Service (port 8081)
- Management of the lifecycle of borrowing transactions and reservations
- Enforcing borrowing limits (max 3 active loans per member)
- Calculating late return fines
- Tracking borrowing history of members
- Persistence via JPA with H2 in-memory database

### 4. Notification and Statistics Service (port 8083)
- Borrowing statistics
- Generating reports for librarians on book popularity
- Handling notifications for overdue books
- Asynchronous communication with Lending Service via Apache ActiveMQ Artemis

---

## Asynchronous Communication (JMS)
The **Lending Service** produces messages to an ActiveMQ Artemis queue when a loan is created or a fine is issued.
The **Notification and Statistics Service** consumes these messages asynchronously and stores them in its database.

The JMS configuration is externalized in `application.yml` of each service.

---

## Use Case Diagram
![image](docs/pa165_use_case.svg)

**User Roles:**
- **Anonymous User** — can browse and search the catalogue, create an account
- **Registered Member** — can reserve/borrow/return books, manage borrowings, edit account
- **Librarian** — can manage catalogue, handle overdue returns, view statistics

---

## DTO Class Diagram
![image](docs/pa165_class_dto_diagram.svg)

**Book Service**
- `BookDto` — id, title, author, isbn, genre, description, availableCopies, yearPublication

**Member Service**
- `MemberDto` — id, username, email, registrationDate
- `NewMemberRegisteringDto` — username, email, password

**Lending Service**
- `LoanDto` — id, memberId, bookCopyId, borrowDate, dueDate, returnDate, status
- `ReservationDto` — id, memberId, bookId, reservationDate, whenAvailableDate, status
- `FineDto` — id, memberId, loanId, date, amount, status

**Notification and Statistics Service**
- `NotificationDto` — id, message, recipientEmail, createdAt
- `CreateNotificationDto` — message, recipientEmail
- `StatsDto` — id, bookTitle, returned
- `PopularBookDto` — bookTitle, borrowCount

---

## GenAI Usage
GenAI (ChatGPT, Claude Sonnet) was used for brainstorming architecture ideas, generating boilerplate
code examples, and checking test case correctness. All code was reviewed, written, and adjusted
manually in the IntelliJ IDEA IDE.