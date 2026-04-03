# 📚 Book Search Application

A full-stack book search application built with **Spring Boot**, **PostgreSQL**, and **React (Vite)**.
It features **unified search**, **fuzzy autocomplete suggestions**, and a fully **Dockerized setup** for easy local development and deployment.

---

## /+/ Features

###  Unified Book Search

* Search by **title or author** from a single search bar
* Backend handles:

  * Merging results
  * Deduplication
  * Sorting
  * Pagination

---

###  Fuzzy Autocomplete Suggestions

* Real-time suggestions while typing
* Typo-tolerant matching using **Levenshtein distance**
* Combines:

  * Prefix matching
  * Substring matching
* Improves UX significantly

---

###  PostgreSQL Database

* Schema managed via **Flyway migrations**
* Persistent storage using Docker volumes

---

###  Fully Dockerized

* React frontend served via **Nginx**
* Spring Boot backend (**Java 21**)
* One-command startup using **Docker Compose**

---

###  Modern Frontend

* Built with **React + Vite**
* Autocomplete dropdown with debounce
* Clean and responsive UI

---

##  Tech Stack

### Backend

* Java 21
* Spring Boot 3.2.x
* Spring Data JPA
* Flyway
* PostgreSQL
* Gradle

### Frontend

* React
* Vite
* Axios
* Nginx (production build)

### Infrastructure

* Docker
* Docker Compose

---

##  Architecture Overview

```
Browser
  │
  ▼
React Frontend (localhost:3000)
  │
  ▼
Spring Boot API (localhost:8080)
  │
  ▼
PostgreSQL Database
```

---

##  Key Design Principles

* Backend-driven search logic (**thin frontend**)
* Clear separation between:

  * Search results
  * Autocomplete suggestions
* Stateless frontend
* Production-style Docker setup

---

##  Search & Fuzzy Logic

### Unified Search

A single endpoint handles:

* Full-text search
* Author search
* Result merging & deduplication
* Sorting and pagination

---

### Autocomplete Suggestions

* Triggered while typing
* Lightweight backend endpoint
* Returns **title suggestions only**

---

### Fuzzy Matching Strategy

1. **Level 1: Prefix & Substring Matching**

   * Uses SQL `ILIKE %query%`

2. **Level 2: Typo Tolerance**

   * Uses **Levenshtein edit distance**

3. **Fallback Logic**

   * Fuzzy matching only applied if needed
   * Results ranked by closest match

---

##  Project Structure

```
.
├── docker-compose.yml
├── Dockerfile                  # Backend Dockerfile
├── src/                        # Spring Boot source
├── book-search-frontend/       # React frontend
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── index.html
│   ├── package.json
│   └── src/
└── import/                     # DB import script
```

---

## ▶️ Running the Project

###  Prerequisites

* Docker Desktop
* Docker Compose

---

###  Start Everything

From the project root:

```bash
docker compose up --build
```

---

###  Access the Application

* Frontend: http://localhost:3000
* Backend API: http://localhost:8080

---

### Database Migrations

* Managed via **Flyway**
* Automatically executed on backend startup

---

##  What This Project Demonstrates

* Full-stack application design
* Clean backend architecture
* Real-world fuzzy search implementation
* Docker setup & troubleshooting
* Frontend-backend integration
* Production-style configuration

---

##  Possible Improvements

* Author autocomplete suggestions
* Keyboard navigation for dropdown
* Popularity-based ranking
* Authentication & user accounts
* CI/CD pipeline

---

##  License

This project is for educational and demonstration purposes.
