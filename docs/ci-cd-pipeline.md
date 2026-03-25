# UniEvent Campus Project - CI/CD Pipeline

This document describes the Continuous Integration and Continuous Deployment (CI/CD) pipeline for the UniEvent Campus Project. The current pipelines focus mainly on the **backend** (Java Spring Boot + Maven + Docker), but they provide a scalable structure to add frontend workflows in the future.

## 1. CI Workflow (`ci.yml`)

The CI (Continuous Integration) workflow guarantees that new code securely integrates and passes all tests before being merged or evaluated.

- **Triggers:**
  - `push` to `main` and `develop` branches.
  - `pull_request` targeting `main` and `develop` branches.
- **Paths Filter:** Only triggers when there are changes inside the `backend/` directory or the workflow file itself.

### Steps
1. **Checkout code:** Pulls the repository code using `actions/checkout@v4`.
2. **Setup JDK 17:** Sets up the Eclipse Temurin JDK 17 and enables Maven caching to speed up subsequent builds.
3. **Maven Build & Test:** Executes `mvn clean verify` which runs unit tests and creates the `jar` file.
4. **Test Docker Build:** Executes `docker build -t unievent-backend:test .` to ensure the application correctly containerizes without actually pushing the image.

## 2. CD Workflow (`cd.yml`)

The CD (Continuous Deployment) workflow manages artifact creation, Docker image building, and uploading to the registry.

- **Triggers:**
  - `push` to the `main` branch.
- **Paths Filter:** Only triggers when there are changes to the `backend/` directory or the workflow file.

### Steps
1. **Checkout code:** Pulls the latest main branch code.
2. **Setup JDK 17:** Prepares the build environment.
3. **Maven Package:** Compiles and packages the application via `mvn clean package -DskipTests` (we skip tests here since they already ran during CI).
4. **Login to GHCR:** Logs into the GitHub Container Registry (`ghcr.io`) automatically using the repository's `GITHUB_TOKEN`.
5. **Docker Metadata Extraction:** Calculates dynamic tags and labels for the new image (e.g., `latest` and a unique commit `sha`).
6. **Docker Build & Push:** Uses Docker's build-push-action to cross-build (if needed) and push the final `unievent-backend` image to `ghcr.io`.

## Future Work & Deployment
Currently, the pipeline creates the container image and pushes it to GHCR. There is a commented-out template block inside `cd.yml` for actual machine deployment (using SSH and Docker Compose) that can be activated once a production virtual machine (AWS EC2, DigitalOcean, etc.) is configured.
