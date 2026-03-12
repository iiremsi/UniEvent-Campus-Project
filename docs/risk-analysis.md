**PART 1: Risk Analysis**

**1.1 Risk Identification**

Potential risks that may be encountered during the project are divided into three main categories:

  **Technical Risks:**

      Server, database, or cloud infrastructure downtime.

      Database connection pool exhaustion or performance bottlenecks in API endpoints.

      Newly discovered zero-day vulnerabilities in third-party dependencies (Spring Security, JWT libraries, database drivers, etc.).

      Deployment issues due to Docker containerization or Kubernetes configuration errors.

  **Operational Risks:**

      Lack of communication and integration delays between Backend, Frontend, and DevOps teams (Sprint processes).

      Failure to meet the planned Scrum schedule / deadlines.

      Frequent or sudden changes in requirements (Scope creep).

  **Security Risks:**

      Account Takeover of user (Student/Club/Admin) accounts.

      Unauthorized access to API endpoints or unauthorized data leakage (Data Breach).

      Malicious users overwhelming the platform with spam event streams (Denial of Service).

      JWT token interception or Man-in-the-Middle (MITM) attacks.

**1.2 Risk Assessment and Management**

Below are the probability and impact (scored from 1-5) assessments of the identified prominent risks, along with the mitigation strategies:

1\.  **System/Database Downtime**

      **Probability:** Medium (3) \| **Impact:** Critical (5)

      **Mitigation Strategy:** Regular PostgreSQL backups, automated recovery with Liveness/Readiness probes of pods in the Kubernetes environment, and Replica set configuration.

2\.   **Data Breach & Unauthorized Access**

      **Probability:** Low (2) \| **Impact:** Critical (5)

      **Mitigation Strategy:** Use of Stateless JWT, tightened input validation, password hashing, and endpoint security with Spring Security configs.

3\.   **Spam Event Sharing / Abuse**

      **Probability:** High (4) \| **Impact:** Medium (3)

      **Mitigation Strategy:** Implement rate limiting, impose character limits on posts (max 280), and actively use Admin logging/moderator deletion authorities.

4\.   **Integration Delays (Backend - Frontend)**

      **Probability:** High (4) \| **Impact:** High (4)

      **Mitigation Strategy:** Ensure the API contract is always kept up-to-date via Swagger UI and daily tracking via Jira Agile board (Daily Scrum).

**1.3 Mitigation/Preventive Actions**

  **R1 - Solution for Infrastructure Errors:** Enforcing Test (Unit and Integration) steps in CI/CD pipeline processes. Problematic builds are prevented from being deployed to the server on the pipeline.

  **R2 - Solution for Security Vulnerabilities:** Regular auditing of dependency versions in \`pom.xml\`. Coding DTO/Controller validations to protect the application against SQL Injection and XSS attacks.

  **R3 - Solution for Operational Errors:** Enforcing GitHub branch protection rules (mandatory code review on Pull Requests). Improving communication processes with Retrospective meetings at the end of each sprint.

**1.4 Risk Matrix**

\| Probability \\ Impact \| Low (1-2) \| Medium (3) \| High (4) \| Critical (5) \|

\| :\-\-- \| :\-\-- \| :\-\-- \| :\-\-- \| :\-\-- \|

\| **Very High (5)** \| \| \| \| \|

\| **High (4)** \| \| Spam Post Sharing \| Integration Delay \| \|

\| **Medium (3)** \| Scope Change \| \| \| Infrastructure/Server Downtime \|

\| **Low (2)** \| \| \| Zero-Day Library Vulnerability \| Data Breach\|

\| **Very Low (1)** \| \| \| \| \|

  **Red Zone (Critical/High Impact - Medium/High Probability):** Priority risks requiring immediate action and close monitoring (e.g., Integration, Server Downtime).

  **Yellow Zone (Medium Impact - Medium Probability):** Risks requiring periodic monitoring and control mechanisms (e.g., Spam posts).

  **Green Zone (Low Impact/Probability):** Risks that are acceptable or manageable with minimal precautions.

**PART 2: Code Security & Data Privacy**

1\.   **Strong Authentication & Authorization:**

      The system uses a stateless architecture based on Spring Security and **JWT (JSON Web Token)** .

      Endpoints are restricted based on URLs. Only users authorized for their roles (Student, Club, Admin) can perform relevant operations (e.g., Only a Club account can create an event).

2\.   **Input Validation:**

      All data coming from the user is filtered at the Controller level with Jakarta Bean Validation (\`@Valid\`, \`@NotBlank\`, \`@Size\`, etc.).

      This way, the application rejects harmful data as a 400 Bad Request long before it reaches the database, erecting a wall against attacks like SQL Injection.

3\.   **Global Exception Handling:**

      By using \`@ControllerAdvice\` and \`GlobalExceptionHandler\` architecture, internal application technical errors (stack trace, database query error, etc.) are prevented from leaking to the outside world.

      Invalid requests only return a meaningful JSON error message (Error response standardization).

4\.   **CORS & CSRF Protection:**

      Thanks to the stateless JWT API design, there is natural protection against CSRF (Cross-Site Request Forgery) attacks because no session is kept on the server.

      CORS settings (via Global config) can be restricted to open only to approved frontend origins (e.g., \`http://localhost:3000\`).

5\.   **Password Security (Password Hashing):**

      User passwords are not stored directly (plaintext) in the database. They are hashed using a strong **BCryptPasswordEncoder** algorithm with salt during registration.

**How is User Data Protected? (Data Privacy)**

1\.   **Data Minimization Principle:**

      When a user registers on the UniEvent platform, only the mandatory information required for the core functions of the application (email, password, role, student ID, etc.) is collected. Extra sensitive data is not collected.

2\.   **Database Isolation and Data Visibility:**

      In the application\'s business logic (Service layer), users\' direct access to others\' private profiles/details is restricted; only public \"Event/Post\" shares are presented.

      Database access passwords are never kept hardcoded in the code. They are securely injected in the Docker environment via environment variables (\`.env\`).

3\.   **Token Security:**

      The JWT payload does not contain unnecessary or sensitive data (such as user password hash). It only includes minimum claims sufficient for authentication (username/email, role, etc.).

4\.   **Connection Encryption:**

      By planning for the application to communicate over the TLS/SSL (HTTPS) protocol via an Ingress or Reverse Proxy in the production environment (on K8s), sniffing of the data in transit between the API and Frontend is prevented.

## **PART 3: Implementation Roadmap**

UniEvents follows a 6-week Agile sprint model with clear deliverables at each stage.

### **1. Infrastructure Setup** {#infrastructure-setup}

Initialize project environment with Spring Boot (backend), React with Vite (frontend), and PostgreSQL via Docker. Create a User entity supporting dual account types (Student/Club). Setup GitHub repository with branch protection and Jira board for task tracking.

### **2. Authentication System** {#authentication-system}

Implement secure user authentication with register/login endpoints. Use BCrypt for password hashing to address security risk S-04. Configure Spring Security and CORS to enable frontend-backend communication. Build Sign Up and Login pages with form validation.

### **3. Core Event & Post Management** {#core-event-post-management}

Create Event and Post entities with full CRUD operations. Implement authorization logic ensuring only Club accounts can create content. Build Feed page displaying posts chronologically and Event list page. Add database indexes on foreign keys and timestamps to optimize query performance.

### **4. Social Features** {#social-features}

Implement a Follow system allowing students to customize their feed. Add Like functionality for post engagement. Integrate MediaFile entity for image/video uploads (storing URLs, not binary data). Create Club profile pages with bio, cover photo, and event history.

### **5. Testing & Refinement** {#testing-refinement}

Write unit tests for Service layer targeting 70%+ coverage. Conduct User Acceptance Testing with 5-10 students to validate usability. Perform load testing with JMeter to ensure the system handles 100+ concurrent users. Execute security audit checking SQL Injection, XSS, and authorization bypass attempts.

### **6. Deployment & Documentation** {#deployment-documentation}

Finalize Docker Compose configuration with health checks and automatic restart policies. Setup GitHub Actions CI/CD pipeline for automated testing on every commit. Complete documentation including README, API endpoint descriptions, Risk Analysis report, and Testing Plan. Prepare live demo and presentation materials.
