# RegIntel AI — Backend

Agentic AI platform that converts regulatory documents into actionable enterprise impact and engineering plans.

## Tech Stack

- Java 21
- Spring Boot 3.4.x
- Maven
- PostgreSQL 16
- JPA/Hibernate
- Lombok
- Bean Validation
- SpringDoc OpenAPI (Swagger)

## Project Structure

```
backend/
├── pom.xml
├── docker-compose.yml
├── src/main/java/com/regintel/ai/
│   ├── RegIntelAiApplication.java
│   ├── common/
│   │   ├── config/          # OpenAPI, JPA auditing
│   │   ├── dto/             # ApiResponse, ErrorResponse
│   │   ├── entity/          # BaseEntity
│   │   └── exception/       # Global exception handling
│   ├── regulation/          # Regulatory document CRUD
│   ├── regulationintelligence/  # Regulation analysis
│   ├── enterpriseimpact/    # Business impact assessments
│   ├── engineeringplanning/ # Engineering plans & tasks
│   ├── executivereporting/  # Executive reports
│   └── agentorchestration/  # Agent workflow orchestration
└── src/main/resources/
    ├── application.yml
    ├── application-dev.yml
    ├── application-prod.yml
    └── db/schema.sql
```

Each domain module follows: `controller` → `service` → `repository` → `entity` + `dto`.

## Prerequisites

- JDK 21
- Maven 3.9+
- Docker & Docker Compose (for PostgreSQL)

## Run Instructions

### 1. Start PostgreSQL

```bash
cd backend
docker compose up -d
```

Database: `regintel` | User: `regintel` | Password: `regintel` | Port: `5432`

### 2. Build & Run

```bash
mvn clean package -DskipTests
mvn spring-boot:run
```

Or with a specific profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. Access

| Resource       | URL                              |
|----------------|----------------------------------|
| API Base       | http://localhost:8080/api/v1     |
| Swagger UI     | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON   | http://localhost:8080/api-docs   |

## Standard API Response

```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": { },
  "timestamp": "2026-07-23T17:00:00"
}
```

## REST API Contracts

### Regulation

| Method | Endpoint                    | Description          |
|--------|-----------------------------|----------------------|
| POST   | `/regulations`              | Create regulation    |
| GET    | `/regulations`              | List all             |
| GET    | `/regulations/{id}`         | Get by ID            |
| PUT    | `/regulations/{id}`         | Update               |
| DELETE | `/regulations/{id}`         | Delete               |

### Regulation Intelligence

| Method | Endpoint                                      | Description       |
|--------|-----------------------------------------------|-------------------|
| POST   | `/regulations/{regulationId}/analyses`        | Create analysis   |
| GET    | `/regulations/{regulationId}/analyses`        | List by regulation|
| GET    | `/analyses/{id}`                              | Get by ID         |
| PUT    | `/analyses/{id}`                              | Update            |

### Enterprise Impact Analyst Agent

Consumes **structured `RegulatoryIntelligence` JSON** from a completed regulation analysis (`intelligence_payload` column). Uses a deterministic mock enterprise knowledge base to produce a full impact assessment.

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/regulations/{regulationId}/enterprise-impact/analyze` | Start impact analysis |
| GET | `/regulations/{regulationId}/enterprise-impact` | Get latest assessment |
| GET | `/regulations/{regulationId}/enterprise-impact/applications` | Impacted applications |
| GET | `/regulations/{regulationId}/enterprise-impact/customers` | Impacted customers |
| GET | `/regulations/{regulationId}/enterprise-impact/transactions` | Impacted transactions |
| GET | `/regulations/{regulationId}/enterprise-impact/risk-heatmap` | Risk heatmap |

**Prerequisite:** A `regulation_analyses` record with `status=COMPLETED` and valid `intelligence_payload` JSON matching the `RegulatoryIntelligence` schema.

### Enterprise Impact (Legacy CRUD)

| Method | Endpoint                              | Description        |
|--------|---------------------------------------|--------------------|
| POST   | `/analyses/{analysisId}/impacts`      | Create assessment  |
| GET    | `/analyses/{analysisId}/impacts`      | List by analysis   |
| GET    | `/impacts/{id}`                       | Get by ID          |
| PUT    | `/impacts/{id}`                       | Update             |

### Engineering Delivery Planner Agent

Transforms a completed **EnterpriseImpactAssessment** into an executable delivery plan. Does not re-run regulation or impact analysis.

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/regulations/{regulationId}/delivery-plan/generate` | Generate delivery plan |
| GET | `/regulations/{regulationId}/delivery-plan` | Get delivery plan |
| GET | `/regulations/{regulationId}/delivery-plan/epics` | Get epics |
| GET | `/regulations/{regulationId}/delivery-plan/user-stories` | Get user stories |
| PATCH | `/regulations/{regulationId}/delivery-plan/user-stories/{storyId}/status` | Update story status |

**Prerequisite:** Completed enterprise impact assessment (`POST .../enterprise-impact/analyze`).

**Output schema:** Epic → Features → User Stories → Tasks, plus testing/deployment/rollback strategies and production validation checklist. Mock Jira sync via `JiraAdapter`.

### Engineering Planning (Legacy CRUD)

| Method | Endpoint                          | Description     |
|--------|-----------------------------------|-----------------|
| POST   | `/impacts/{impactId}/plans`       | Create plan     |
| GET    | `/impacts/{impactId}/plans`       | List plans      |
| GET    | `/plans/{id}`                     | Get plan        |
| POST   | `/plans/{planId}/tasks`           | Add task        |
| GET    | `/plans/{planId}/tasks`           | List tasks      |

### Executive Reporting

| Method | Endpoint                | Description     |
|--------|-------------------------|-----------------|
| POST   | `/reports`              | Create report   |
| GET    | `/reports`              | List all        |
| GET    | `/reports/{id}`         | Get by ID       |
| POST   | `/reports/{id}/publish` | Publish report  |

### RegIntel End-to-End Orchestration

Single API to run the full agent pipeline with workflow tracking, audit trail, and retry support.

```
Upload Regulation → Regulation Intelligence → Enterprise Impact → Engineering Delivery → Executive Copilot → Final Analysis
```

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/regintel/analyze` | Run full pipeline (returns `workflowId`) |
| GET | `/regintel/{workflowId}` | Get complete aggregated analysis |
| GET | `/regintel/{workflowId}/status` | Get workflow & step status |
| GET | `/regintel/{workflowId}/executive-report` | Get executive report + full analysis |
| POST | `/regintel/{workflowId}/retry` | Retry from failed step |

**Example request:**
```bash
curl -X POST http://localhost:8080/api/v1/regintel/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "title": "EU Sanctions Amendment 2026",
    "source": "European Commission",
    "jurisdiction": "EU",
    "documentType": "DIRECTIVE",
    "rawContent": "All cross-border SWIFT payment transactions must pass sanctions screening. Trade finance LC documentary checks required.",
    "effectiveDate": "2026-09-01"
  }'
```

### Agent Orchestration (Legacy)

| Method | Endpoint                        | Description      |
|--------|---------------------------------|------------------|
| POST   | `/workflows`                    | Start workflow   |
| GET    | `/workflows`                    | List all         |
| GET    | `/workflows/{id}`               | Get by ID        |
| POST   | `/workflows/{id}/complete`      | Complete workflow|
| POST   | `/workflows/{workflowId}/tasks` | Add agent task   |
| GET    | `/workflows/{workflowId}/tasks`   | List tasks       |

## Database Schema

See `src/main/resources/db/schema.sql` for the full PostgreSQL schema with 8 tables:

- `regulations`
- `regulation_analyses`
- `impact_assessments`
- `engineering_plans` / `engineering_tasks`
- `executive_reports`
- `agent_workflows` / `agent_tasks`

## Profiles

| Profile | Use Case                          | DDL Mode  |
|---------|-----------------------------------|-----------|
| `dev`   | Local development (default)       | `update`  |
| `prod`  | Production (env vars for DB creds)| `validate`|

## Example: End-to-End Flow

```bash
# 1. Create a regulation
curl -X POST http://localhost:8080/api/v1/regulations \
  -H "Content-Type: application/json" \
  -d '{"title":"GDPR Article 17","source":"EU","jurisdiction":"EU","documentType":"REGULATION","rawContent":"Right to erasure..."}'

# 2. Start an agent workflow
curl -X POST http://localhost:8080/api/v1/workflows \
  -H "Content-Type: application/json" \
  -d '{"name":"GDPR Processing Pipeline","workflowType":"FULL_ANALYSIS","regulationId":"<regulation-uuid>"}'
```

## Notes

- AI/agent logic is **not implemented** yet — workflows and tasks are scaffolded for future integration.
- JPA auditing auto-manages `createdAt` / `updatedAt` timestamps.
- Validation errors return structured `fieldErrors` in the error response.
