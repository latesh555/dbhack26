-- RegIntel AI - Initial Database Schema
-- PostgreSQL 16+

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================
-- Regulation Module
-- ============================================================
CREATE TABLE IF NOT EXISTS regulations (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title           VARCHAR(500) NOT NULL,
    source          VARCHAR(255) NOT NULL,
    jurisdiction    VARCHAR(100) NOT NULL,
    document_type   VARCHAR(100) NOT NULL,
    raw_content     TEXT,
    status          VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    effective_date  DATE,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_regulations_status ON regulations(status);
CREATE INDEX IF NOT EXISTS idx_regulations_jurisdiction ON regulations(jurisdiction);

-- ============================================================
-- Regulation Intelligence Module
-- ============================================================
CREATE TABLE IF NOT EXISTS regulation_analyses (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    regulation_id       UUID NOT NULL REFERENCES regulations(id) ON DELETE CASCADE,
    summary             TEXT,
    key_requirements    TEXT,
    compliance_areas    TEXT,
    risk_level          VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
    status              VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    analyzed_at         TIMESTAMP,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_regulation_analyses_regulation_id ON regulation_analyses(regulation_id);
CREATE INDEX IF NOT EXISTS idx_regulation_analyses_status ON regulation_analyses(status);

-- ============================================================
-- Enterprise Impact Module
-- ============================================================
CREATE TABLE IF NOT EXISTS impact_assessments (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    regulation_analysis_id  UUID NOT NULL REFERENCES regulation_analyses(id) ON DELETE CASCADE,
    business_unit           VARCHAR(255) NOT NULL,
    impact_type             VARCHAR(50) NOT NULL,
    severity                VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
    description             TEXT,
    estimated_cost          DECIMAL(15, 2),
    status                  VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_impact_assessments_analysis_id ON impact_assessments(regulation_analysis_id);
CREATE INDEX IF NOT EXISTS idx_impact_assessments_severity ON impact_assessments(severity);

-- ============================================================
-- Engineering Planning Module
-- ============================================================
CREATE TABLE IF NOT EXISTS engineering_plans (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    impact_assessment_id    UUID NOT NULL REFERENCES impact_assessments(id) ON DELETE CASCADE,
    title                   VARCHAR(500) NOT NULL,
    description             TEXT,
    priority                VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
    estimated_effort_days   INTEGER,
    status                  VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_engineering_plans_impact_id ON engineering_plans(impact_assessment_id);
CREATE INDEX IF NOT EXISTS idx_engineering_plans_status ON engineering_plans(status);

CREATE TABLE IF NOT EXISTS engineering_tasks (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    engineering_plan_id UUID NOT NULL REFERENCES engineering_plans(id) ON DELETE CASCADE,
    title               VARCHAR(500) NOT NULL,
    description         TEXT,
    priority            VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
    estimated_hours     INTEGER,
    status              VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_engineering_tasks_plan_id ON engineering_tasks(engineering_plan_id);

-- ============================================================
-- Executive Reporting Module
-- ============================================================
CREATE TABLE IF NOT EXISTS executive_reports (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title           VARCHAR(500) NOT NULL,
    report_type     VARCHAR(50) NOT NULL,
    content         TEXT,
    regulation_id   UUID REFERENCES regulations(id) ON DELETE SET NULL,
    status          VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    generated_at    TIMESTAMP,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_executive_reports_type ON executive_reports(report_type);
CREATE INDEX IF NOT EXISTS idx_executive_reports_regulation_id ON executive_reports(regulation_id);

-- ============================================================
-- Agent Orchestration Module
-- ============================================================
CREATE TABLE IF NOT EXISTS agent_workflows (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name            VARCHAR(255) NOT NULL,
    workflow_type   VARCHAR(100) NOT NULL,
    status          VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    current_step    VARCHAR(100),
    regulation_id   UUID REFERENCES regulations(id) ON DELETE SET NULL,
    started_at      TIMESTAMP,
    completed_at    TIMESTAMP,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_agent_workflows_status ON agent_workflows(status);
CREATE INDEX IF NOT EXISTS idx_agent_workflows_regulation_id ON agent_workflows(regulation_id);

CREATE TABLE IF NOT EXISTS agent_tasks (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workflow_id     UUID NOT NULL REFERENCES agent_workflows(id) ON DELETE CASCADE,
    agent_type      VARCHAR(100) NOT NULL,
    task_type       VARCHAR(100) NOT NULL,
    status          VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    input_payload   TEXT,
    output_payload  TEXT,
    error_message   TEXT,
    started_at      TIMESTAMP,
    completed_at    TIMESTAMP,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_agent_tasks_workflow_id ON agent_tasks(workflow_id);
CREATE INDEX IF NOT EXISTS idx_agent_tasks_status ON agent_tasks(status);
