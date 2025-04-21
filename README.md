# 📚 School Library App - Backend
**Author: Anna Adamik**

## Description
A responsive web application for managing a school library system. Librarians can browse and manage the book inventory, as well as view and update user data through a dedicated administration panel.

This frontend is built with Angular and communicates with a RESTful API provided by the backend application.

🔗 **Live Demo:** https://school-library.ovh/

---

## 🛠 Tech Stack
- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA + Hibernate**
- **MySQL + H2**
- **Liquibase** (for DB migrations)
- **JUnit, Mockito, AssertJ** (for testing)
- **Project Lombok**
- **Open Library API**
- **Maven**
- **Docker**
- **Nginx Proxy Manager** (deployment)

---

## 🌐 API Overview

### Book Management
- `GET /api/books` – Fetches a list of books. Optionally filter by title, author, or ISBN using the text query parameter.
- `GET /api/books/{id}` – Fetches details of a book by its ID.
- `POST /api/books` – Creates a new book. Body: BookDto (see below for structure).
- `PUT /api/books/{id}` – Updates a book by its ID. Body: BookDto (see below for structure).
- `DELETE /api/books/{id}` – Deletes a book by its ID.
- `GET /api/books/details/{isbn}` - Fetches book details by ISBN from Open Library API.
- `GET /api/books/{id}/loans` - Fetches loan history for a book by its ID.

BookDto Structure:
- id: The unique identifier for the book (Long).
- title: The title of the book (String).
- author: The author of the book (String).
- isbn: The ISBN number of the book (String).
- genre: The genre of the book (String).

### User Management
- `GET /api/users` – Fetches a list of users. Optionally filter by last name using the lastName query parameter.
- `GET /api/users/{id}` – Fetches details of a user by their ID.
- `POST /api/users` - Creates a new user. Body: UserDto (see below for structure).
- `PUT /api/users/{id}` - Updates a user by their ID. Body: UserDto (see below for structure).
- `DELETE /api/users/{id}` - Deletes a user by their ID.
- `GET /api/users/{id}/loans` - Fetches loan history for a user by their ID.

UserDto Structure:
- id: The unique identifier for the user (Long).
- firstName: The first name of the user (String).
- lastName: The last name of the user (String).
- pesel: The PESEL number of the user (String), must be 11 digits.

### Genre
- `GET /api/genres/names` - Fetches a list of all genre names.

### Loan
- `POST /api/loans` - Creates a new loan. Body: LoanDto (see below for structure).
- `POST /api/loans/{id}/finish` - Marks a loan as finished by its ID. Returns: The finish date of the loan.

LoanDto Structure:
- id: The unique identifier for the loan (Long).
- userId: The ID of the user associated with the loan (Long).
- bookId: The ID of the book associated with the loan (Long).

---

> This project is part of a full-stack system. Frontend repo: [github.com/Ana-ada-code/libraryapp](https://github.com/Ana-ada-code/libraryapp)
