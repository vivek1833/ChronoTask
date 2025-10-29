# 🕓 ChronoTask – Distributed Task Scheduler

ChronoTask is a **backend-only distributed scheduling system** built with **Spring Boot**, **Kafka**, and **PostgreSQL**.  
It allows users to **create tasks with due dates**, and automatically **notifies users every morning** about any **overdue tasks** through an event-driven architecture.

---

## 🚀 Overview

ChronoTask is designed to demonstrate how modern backend systems handle **delayed executions**, **reliable scheduling**, and **asynchronous communication** using message brokers.

The system runs entirely without a frontend — all interactions can be tested via **Postman** or **cURL**.  
It is composed of three independent microservices communicating via **Kafka**.

---

## 🧩 Architecture

```
 ┌────────────────────┐       ┌───────────────────────┐       ┌─────────────────────┐
 │ Task API Service   │ --->  │ Scheduler Service     │ --->  │ Worker Service      │
 │ (Create Tasks)     │       │ (Detect Overdue Tasks)│       │ (Send Notifications)│
 └────────────────────┘       └───────────────────────┘       └─────────────────────┘
           │                           │                            │
           └──────────────→ PostgreSQL ←┴──────────────→ Kafka ←─────┘
```

---

## 🏗️ Components

### 1. **Task API Service**

Handles creation, assignment, and management of tasks.

**Endpoints**
| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/api/users` | Create a new user |
| `POST` | `/api/tasks` | Create a new task |
| `GET` | `/api/tasks/{id}` | Get task by ID |
| `GET` | `/api/tasks/pending` | Get overdue or pending tasks |
| `PUT` | `/api/tasks/{id}/complete` | Mark a task as completed |

---

### 2. **Scheduler Service**

Runs a daily CRON job (9:30 AM) to:

- Fetch tasks whose due date < current date and not completed.
- Publish `TaskOverdueEvent` messages to Kafka.

**Technologies:** Spring Scheduler, Kafka Producer, PostgreSQL.

---

### 3. **Worker Service**

Consumes messages from Kafka topic `task-overdue-topic` and:

- Sends email notifications (mocked via console or Mailtrap) to the **task creator** and **assignee**.
- Updates task status to reflect notification sent.

**Technologies:** Kafka Consumer, Spring Mail.

---

## 🧱 Tech Stack

| Layer            | Technology                    |
| ---------------- | ----------------------------- |
| Framework        | Spring Boot 3                 |
| Database         | PostgreSQL                    |
| Message Broker   | Apache Kafka                  |
| Build Tool       | Maven                         |
| Containerization | Docker & Docker Compose       |
| Email            | Spring Mail (SMTP / Mailtrap) |
| Language         | Java 17                       |

---

## ⚙️ How It Works

1. **User creates a task** with title, description, assignee, and due date via REST API.
2. **SchedulerService** runs daily at 9:30 AM:
   - Queries for overdue tasks.
   - Publishes task IDs to Kafka.
3. **WorkerService** listens to Kafka:
   - Fetches task details from DB.
   - Sends email notifications to both creator and assignee.

---

## 🧠 Key Design Concepts

- **Event-Driven Architecture** using Kafka
- **Microservice Communication** with decoupled modules
- **Time-Based Scheduling** using CRON triggers
- **Reliable Message Delivery** with retry mechanism
- **PostgreSQL Indexing** for efficient overdue-task lookups

---

## 🧰 Project Structure

```
chronotask/
├── task-api-service/
│   └── src/main/java/com/chronotask/api/
├── scheduler-service/
│   └── src/main/java/com/chronotask/scheduler/
├── worker-service/
│   └── src/main/java/com/chronotask/worker/
├── docker-compose.yml
├── README.md
└── postman_collection.json
```

---

## 📦 Setup & Run

### Prerequisites

- Docker & Docker Compose
- Java 17
- Maven 3+

### Steps

```bash
# Clone repository
git clone https://github.com/vivek1833/ChronoTask.git
cd ChronoTask

# Start Postgres + Kafka
docker-compose up -d

# Start each service
cd task-api-service && mvn spring-boot:run
cd ../scheduler-service && mvn spring-boot:run
cd ../worker-service && mvn spring-boot:run
```

---

## 📬 Testing (via Postman)

- Import `postman_collection.json`
- Create users → Create tasks → Wait for scheduler to trigger
- Overdue task notifications will appear in console or Mailtrap

---

## 🧾 Example Task Payload

```json
{
  "title": "Prepare System Design Notes",
  "description": "Review distributed scheduling concepts",
  "creatorId": 1,
  "assigneeId": 2,
  "dueDate": "2025-10-20T10:00:00"
}
```

---

## 🧠 Future Enhancements

- Add recurring tasks (CRON-like repeat schedule)
- Add authentication (JWT + Spring Security)
- Create simple React dashboard
- Add Prometheus metrics & Grafana monitoring
- Retry queue & dead-letter handling for failed notifications

---
