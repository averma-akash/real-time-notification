# 📡 Real-Time Notification Service

A powerful **backend-only** microservice that delivers **real-time notifications** to clients via **WebSocket** using **Kafka**, **Redis**, and **Spring Boot**. Integrated with **JWT authentication**, **OAuth2**, and full observability via **Prometheus + Grafana**.

---

## 🔧 Tech Stack

- **Java 17** + **Spring Boot 3**
- **WebSocket + STOMP**
- **Apache Kafka** with Retry & Dead Letter Queue
- **Redis** for caching notifications
- **JWT (HttpOnly Cookie)** for security
- **OAuth2 Resource Server (custom)**
- **Oracle DB** for user auth
- **Prometheus + Grafana** for metrics
- **Swagger** for REST API docs
- **Docker + Docker Compose**

---

## 🚀 Features

- ✅ Secure login with JWT (stored in cookies)
- ✅ WebSocket push with STOMP topic subscription
- ✅ Kafka consumer with retry + DLQ handling
- ✅ Redis integration to store and fetch notifications
- ✅ Centralized exception handling
- ✅ Prometheus metrics and Grafana dashboard
- ✅ Swagger UI for REST API testing

---

## 📂 Project Structure


---

## 🌐 API & WebSocket Endpoints

| Endpoint                             | Description                           |
|--------------------------------------|---------------------------------------|
| `POST /auth/login`                   | User login (returns JWT in cookie)    |
| `POST /api/notify/send`              | Send notification to WebSocket topic  |
| `GET /actuator/prometheus`           | Prometheus metrics scraping endpoint  |
| `ws://localhost:8080/ws-notification`| WebSocket endpoint for STOMP          |

---

## 🧪 Testing WebSocket

### ✅ WebSocket URL
** ws://localhost:8080/ws-notification/websocket **

### ✅ Subscribe to Topic
Send the following STOMP frame:
SUBSCRIBE id:sub-0 destination:/topic/notifications


### 🔗 Online Testing Tool
Try it with: [https://www.piesocket.com/websocket-tester](https://www.piesocket.com/websocket-tester)

---

## 🔐 JWT Auth Flow

- Call `POST /auth/login` with username/password.
- On success, you’ll receive JWT in a `Set-Cookie` header:
- Token is validated using OAuth2 Resource Server on every request.

---

## 🔁 Kafka Topics Used

| Topic Name                  | Purpose                          |
|----------------------------|-----------------------------------|
| `notification-events`      | Raw notification messages         |
| `notification-topic`       | Main processing topic             |
| `notification-topic.retry` | For failed retries                |
| `notification-topic.dlq`   | Dead Letter Queue for inspection  |

---

## 📊 Observability

### Prometheus

- URL: [http://localhost:9090](http://localhost:9090)
- Job Config:
```yaml
- job_name: 'real-time-notification'
  metrics_path: '/actuator/prometheus'
  static_configs:
    - targets: ['real-time-notification:8080']

##🐳 Docker Commands

 * docker-compose up -d --build
 * docker-compose down -v

###📦 Important URLs
* Service	URL
* Spring Boot	http://localhost:8080
* Swagger UI	http://localhost:8080/swagger-ui.html
* Prometheus	http://localhost:9090
* Grafana	http://localhost:3000 --> Default admin, admin
* WebSocket Tool	https://www.piesocket.com/websocket-tester