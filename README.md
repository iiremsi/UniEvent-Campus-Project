# 🎓 UniEvent — Campus Event Sharing Platform

UniEvent is a web-based campus event platform where students and clubs share events as short posts, discover upcoming activities, and stay connected with their university community.

> Developed as part of the **Software Project Management** course using Agile (Scrum) methodology.

---

## ✨ Features

- 📝 **Post Events** — Share campus events in short, tweet-like posts (max 280 characters)
- 📰 **Live Feed** — Browse all events in a paginated timeline
- 🔐 **Secure Auth** — JWT-based registration and login
- 👥 **User Roles** — Student, Club, and Admin roles
- 📱 **API-First** — RESTful backend ready for any frontend

---

## 🚀 Quick Start

```bash
cd devops
docker-compose up -d
```
- **API:** http://localhost:8080/api
- **Swagger UI:** http://localhost:8080/swagger-ui.html

---

## 🗂️ Project Structure

```
├── backend/     → Spring Boot 3 REST API + Dockerfile
├── frontend/    → React.js (separate team)
├── devops/      → Docker Compose & Kubernetes manifests
└── docs/        → Technical documentation
```

---

## Risk Analysis
You can find the detailed risk analysis of the project here:
docs/risk-analysis.md

## Validation and Testing Plan
Detailed testing and validation strategy is explained here:
docs/testing-plan.md

---

## 📄 Documentation

| Document | Description |
|---|---|
| [API Spec](docs/api-spec.md) | Endpoints, request/response examples |
| [ER Diagram](docs/er-diagram.md) | Database schema & design decisions |
| [Architecture](docs/architecture.md) | System design, K8s topology, rationale |
| [Risk Analysis](docs/risk-analysis.md) | Technical, Project, User-Realated Risks and Strategies |
| [Validation & Testing  Plan](docs/testing-plan.md) | Unit, Integration, System, User Testing |

---

## 👥 Team

| Role | Responsibility |
|---|---|
| Project Manager & Backend | Backend API, DevOps, database design |
| Frontend  & Requirements Analyst | React UI, user stories |
| QA & Integration Developer | Testing, CI/CD, integration |

---

## 📊 Project Management

- **Methodology:** Agile (Scrum)
- **Tracking:** [Jira Board](https://www.atlassian.com/software/jira)
- **Project Plan:** [UniEvent-Project-Plan.xlsx](https://github.com/user-attachments/files/25415136/UniEvent-Project-Plan.xlsx)

![UML Diagram](https://github.com/user-attachments/assets/8be46d71-e730-42b8-8723-de90fbf6755a)

<details>
<summary>📸 Jira Sprint Planning</summary>
<img width="1600" height="557" alt="Jira Planning" src="https://github.com/user-attachments/assets/95133275-3445-4c18-adbe-b27d3c9d3659" />
</details>
