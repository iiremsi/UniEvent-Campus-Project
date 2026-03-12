## **Validation and Testing Plan**

**Unit Testing:  
** Backend services tested with JUnit 5 to verify business logic correctness (e.g., AuthService registration, PostService authorization checks). Front-end components tested with Vitest to ensure UI renders correctly and handles user interactions. Target: 70% backend coverage, 50% frontend coverage.

**Integration Testing:  
** Full API request-response cycles tested using Postman collections. Verify end-to-end flows such as user registration → login → token generation → authenticated API calls. Database integration tested with TestContainers to ensure data persistence and query correctness.

**End-to-End Testing:  
** Manual test scenarios simulating real user journeys:

- Student: Register → Follow clubs → Browse feed → Register for event

- Club: Create account → Create post with image → Manage event registrations

**Performance Testing:  
** Load testing with Apache JMeter simulating 100 concurrent users performing typical operations (browsing feed, liking posts, registering for events). Measure API response times (target: \<200ms average) and page load speeds (target: \<2s). Use Lighthouse for frontend performance audits.

**Security Testing:  
** Attempt SQL Injection attacks on input fields to verify JPA parameterization protects against execution. Verify Student accounts cannot call POST /api/posts endpoint (expect 403 Forbidden). Inspect database to confirm passwords stored as BCrypt hashes, not plain text.

**User Acceptance Testing (UAT):  
** Recruit 5-10 students and 2-3 club representatives to use the platform without guidance. Observe task completion rates and collect feedback via System Usability Scale (SUS) survey. Success threshold: 80%+ task completion, 70%+ satisfaction rating.

### **Validation Methods**

**Functional Validation:  
** Verify all CRUD operations execute correctly and authorization rules prevent unauthorized actions (e.g., Students cannot create posts). Confirm database constraints enforce data integrity (unique emails, foreign key relationships).

**Performance Validation:  
** Measure API response times under load and compare against target (\<200ms). Use PostgreSQL EXPLAIN ANALYZE to identify slow queries and verify indexes are utilized. Monitor memory usage and ensure no leaks during extended operation.

**Security Validation:  
** Confirm all passwords stored as BCrypt hashes by querying the database directly. Test JWT token expiration and verify unauthorized requests receive proper HTTP error codes. Validate CORS allows only approved frontend origin.

**Targets:**

- API response \<200ms

- Test coverage \>70%

- No critical bugs

- UAT satisfaction \>70%

- Live demo successful

## **Success Criteria**

### **Project Success Requirements**

**Functional:  
** User registration/login works, clubs create posts/events, students follow and view feed, event capacity enforced, authorization prevents students from posting.

**Performance:  
** API \<200ms, page load \<2s, handles 100+ concurrent users (verified via JMeter).

**Quality:  
** Backend coverage \>70%, frontend \>50%, zero critical bugs, code reviews mandatory.

**Security:  
** BCrypt password hashing, no SQL Injection/XSS, authorization working, CORS configured.

**User Experience:  
** SUS score \>70, UAT task completion \>80%, responsive design.

**Delivery:  
** Completed in 6 weeks, 100+ commits, documentation on GitHub, live demo successful.

**Project is SUCCESSFUL if:**

- All must-have features work as specified

- Performance targets met (API \<200ms, page load \<2s)

- Security fundamentals implemented (BCrypt, authorization)

- Test coverage \>70% backend, \>50% frontend

- No critical bugs in production

- Live demo completes successfully

- UAT feedback \>70% positive

- Documentation complete
