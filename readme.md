# Spring WebFlux Note-Taking Application

## Technology Stack

### Backend

- **Spring WebFlux**: Reactive programming framework
- **Spring Data R2DBC**: Reactive database access
- **PostgreSQL**: Relational database
- **JWT**: Authentication and authorization
- **Reactor Core**: Async data stream handling

### Frontend

- Designed for SPA (React, Angular, Vue.js)

### Tools

- **Maven**: Dependency management
- **Lombok**: Boilerplate reduction
- **Docker**: Containerization

## Features

### Core Functionality

- User Authentication (JWT)
- Note Management (CRUD)
- Category Organization
- Many-to-many Relationships
- Reactive Architecture

## API Endpoints

### Authentication

```
POST /auth/register
POST /auth/login
```

### User Management

```
GET /users/me
```

### Notes

```
POST /notes
GET /notes
GET /notes/archived
PUT /notes/{noteId}
DELETE /notes/{noteId}
PATCH /notes/{noteId}/archive
```

### Categories

```
POST /categories
GET /categories
DELETE /categories/{categoryId}
```

### Note-Category Relations

```
POST /notes/{noteId}/categories/{categoryId}
DELETE /notes/{noteId}/categories/{categoryId}
```

## Setup

### Requirements

- Java 17+
- Maven
- PostgreSQL
- Docker (optional)

### Quick Start

```bash
git clone https://github.com/your-repo-url.git
mvn clean install
docker-compose up -d
mvn spring-boot:run
```
