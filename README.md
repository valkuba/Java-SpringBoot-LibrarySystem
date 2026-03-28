# PA165-project-team3-group02 - Library Management System

## Project description
The goal of this project is to create the information system that enables a virtual library
to manage their resources e.g. books, services. The services are represented by the ability for the
registered members to reserve a book and then borrow it for pre-defined period of time.
It also handles late return fines and provides statistical reports for librarians.

Project is based on the Spring Boot framework.

---

## Team Members
| Name     | Service      | Role        |
|----------|--------------|-------------|
| Stanislav Krč | Member service         | Developer   |
| Milan Takáč | Lending service         | Developer   |
| Jakub Valeš | Notification and Statistics Service  | Developer   |
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

### Build & Run
```bash
# Build all modules
mvn clean install

# Run a specific service (example)
cd book-service
mvn spring-boot:run
```
Each service exposes its REST API documentation via Swagger UI at the URL listed above.

---

## Architecture
The project is equally divided into 4 microservices. Each microservice for one team member.
The microservices in the project are following:
1. Book service
   - managing complete book inventory and catalogue
   - management of the book metadata (titles, authors, ISBN, genres ...)
   - search and browsing API for both anonymous users and registered members
   - managing number of available copies
2. Member service
   - managing member account (username, email, password ...)
   - sign in/ sign up (authentification)
3. Lending service
   - management of the lifecycle of borrowing transactions and reservations for the books
   - enforcing borrowing limits and calculating late return fines
   - tracking borrowing history of the member
4. Notification and Statistics Service
   - borrowing statistics
   - generating reports for librarians on books popularity
   - handling notifications for overdue books
   - informing librarians when specific administrative actions are required

---

### Use Case Diagram

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
GenAI (ChatGPT, Claude Sonnet) was used for brainstorming architecture ideas and generating boilerplate code examples. 
And also for checking test cases correctness. All code was reviewed, wrote and adjusted manually in the 
IntelliJ IDEA IDE.

