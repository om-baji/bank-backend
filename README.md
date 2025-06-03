# 🧱 Backend Monorepo – Bank, Email & Statement Services

A scalable, event-driven monorepo for financial microservices. Built with a modular architecture in **Java (Spring Boot)** and **Go Fiber**, this system supports secure banking operations, transactional event publishing via **Kafka**, PDF statement generation, and real-time monitoring with **Prometheus + Grafana**.

---

## 📦 Projects Overview

| Service          | Description |
|------------------|-------------|
| `bank`           | Core banking microservice with JWT auth, transaction handling, and Kafka events |
| `email-service`  | Java-based service that listens to Kafka and sends mails via `JavaMailSender` |
| `statement-service` | Go Fiber service that generates PDF statements using GoPDF and uploads to Cloudinary |
| `monitoring`     | Grafana + Prometheus monitoring stack with metrics exposed by each service |

---

## System Architechture

![image](https://github.com/user-attachments/assets/209533a4-ebe1-4c55-8d07-fe623c72a0a0)

---

## 🏦 Bank Service

A modular Spring Boot microservice for core banking operations.

### ✨ Features

- ✅ Account creation & balance fetching
- 🔐 Secure JWT + Refresh Token authentication
- 💸 Transactional APIs with pagination
- 📤 Kafka event publishing (accounts, transactions)
- 📊 `/status` health endpoint with DB summary
- 🛡️ Spring Security-based auth/authorization

### 🔐 API Endpoints

#### 🧾 Auth
| Method | Endpoint    | Description           |
|--------|-------------|-----------------------|
| POST   | `/register` | Register a new user   |
| POST   | `/login`    | Login, return JWT     |
| POST   | `/refresh`  | Renew JWT token       |

#### 🏦 Accounts
| Method | Endpoint                   | Description               |
|--------|----------------------------|---------------------------|
| GET    | `/accounts`                | List all accounts         |
| GET    | `/accounts/{id}`           | Get account by ID         |
| GET    | `/accounts/{id}/balance`   | Get balance for account   |
| POST   | `/accounts`                | Create a new account      |

#### 💸 Transactions
| Method | Endpoint                      | Description                      |
|--------|-------------------------------|----------------------------------|
| GET    | `/transactions`               | Paginated user transactions      |
| GET    | `/transactions/account/{id}`  | Transactions for specific account|
| GET    | `/transactions/{id}`          | Get specific transaction         |
| POST   | `/transaction/execute`        | Execute a new transaction        |

#### ✅ Health
| Method | Endpoint    | Description             |
|--------|-------------|-------------------------|
| GET    | `/status`   | Health + user stats     |

### 🛰️ Kafka Events

| Event                | Topic                        | Triggered On                     |
|----------------------|------------------------------|----------------------------------|
| Account Created      | `banking.account.events`     | New account creation             |
| Transaction Success  | `banking.transaction.events` | Successful transaction           |
| Transaction Failed   | `banking.transaction.failures` | Failed transaction execution   |

---

## 📧 Email Service

A Spring Boot Kafka consumer using `JavaMailSender` to email users with account and statement updates.

### ✉️ Responsibilities

- Listens to:
  - `banking.account.events`
  - `banking.transaction.events`
  - `banking.transaction.failures`
  - `banking.statement.generated`
- Sends:
  - Welcome email
  - Transaction notifications
  - Monthly PDF statement emails

---

## 📄 Statement Service

A high-performance Go Fiber service for generating PDF statements and pushing Kafka events.

### ⚙️ Features

- ⚡ Built with **Go Fiber**
- 🧾 Generates PDF using `GoPDF`
- ☁️ Uploads PDFs to **Cloudinary**
- 📤 Publishes to `banking.statement.generated` Kafka topic
- 🔐 Exposes secure REST endpoints for statement generation

### Example API

```http
POST /statements
Content-Type: application/json

{
  "userId": "abc123",
  "month": "2024-12"
}
````

➡️ Generates a PDF, uploads it, and pushes the event to Kafka (Email service will email it to the user).

---

## 📊 Monitoring with Prometheus & Grafana

Each microservice exposes Prometheus-compatible metrics at:

```
/actuator/prometheus   (Spring Boot)
```

```
/metrics               (Go Fiber via Prometheus middleware)
```

### Setup

* **Prometheus** scrapes metrics from all services
* **Grafana** visualizes system health and KPIs like:

  * Request/transaction throughput
  * DB connection pool stats
  * Kafka message rates
  * Statement generation success/failure
  * Email delivery rates

---

## 🛠️ Running Locally

### 1. Clone the Monorepo

```bash
git clone https://github.com/your-username/ledger-monorepo.git
cd ledger-monorepo
```

### 2. Set Up `.env` or `application.properties` for Java Services

```properties
# Common properties for bank/email services
spring.datasource.url=jdbc:postgresql://localhost:5432/bankdb
spring.datasource.username=postgres
spring.datasource.password=password
jwt.secret=your_jwt_secret
refresh.secret=your_refresh_secret
kafka.bootstrap-servers=localhost:9092
```

For the Go service, use an `.env` file:

```env
CLOUDINARY_URL=cloudinary://API_KEY:API_SECRET@cloudname
KAFKA_BROKER=localhost:9092
STATEMENT_TOPIC=banking.statement.generated
```

---

### 3. Start Services (Optional Docker Compose)

If using Docker:

```bash
docker-compose up --build
```

Else:

* Run each Java service via `mvn spring-boot:run`
* Run Go service via `go run main.go`

---

## 🧪 Testing

* Spring Boot services contain integration tests in `/src/test/java`
* Statement service uses unit-tested PDF generation logic
* Kafka events can be tested with `kafkacat` or mock consumers

---
