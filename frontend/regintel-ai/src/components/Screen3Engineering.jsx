import { useState } from 'react'
import './Screen3Engineering.css'

const stories = [
  {
    id: 'SANC-001',
    priority: 'Critical',
    status: 'In Progress',
    title: 'Update Sanctions Screening Engine',
    owner: 'Arjun Sharma',
    points: 8,
    duration: '2 Days',
    deps: 'SANC-002',
    apis: ['/api/sanctions/screen', '/api/sanctions/watchlist', '/api/transactions/validate'],
    services: ['sanctions-engine', 'watchlist-service', 'transaction-validator'],
    acceptance: 'All 42 new entities must be ingested into the screening engine. Batch processing validated at 10,000 TPS. Zero false negatives on SDN entities.',
    teams: 'Payments Team',
    statusClass: 'status-inprogress',
    priorityClass: 'priority-critical',
    done: false,
  },
  {
    id: 'SANC-002',
    priority: 'Critical',
    status: 'In Progress',
    title: 'Refresh & Sync Sanctions Watchlists',
    owner: 'Priya Nair',
    points: 5,
    duration: '1 Day',
    deps: 'None',
    apis: ['/api/watchlist/update', '/api/watchlist/sync'],
    services: ['watchlist-manager', 'data-ingestion-pipeline'],
    acceptance: 'OFAC SDN, CAPTA, NS-MBS lists fully refreshed. Delta sync operational. Audit log created.',
    teams: 'Compliance Tech',
    statusClass: 'status-inprogress',
    priorityClass: 'priority-critical',
    done: false,
  },
  {
    id: 'SANC-003',
    priority: 'High',
    status: 'To Do',
    title: 'Modify Payment Validation Rules',
    owner: 'Tom Eriksson',
    points: 5,
    duration: '1 Day',
    deps: 'SANC-001',
    apis: ['/api/payments/validate', '/api/rules/engine'],
    services: ['payment-gateway', 'rules-engine', 'notification-service'],
    acceptance: 'Payment validation rules updated with new entity identifiers. Blocked payments queue configured. Alerts triggered for compliance team.',
    teams: 'Payments Team',
    statusClass: 'status-todo',
    priorityClass: 'priority-high',
    done: false,
  },
  {
    id: 'SANC-004',
    priority: 'High',
    status: 'To Do',
    title: 'Execute Regression & Integration Tests',
    owner: 'Mei Lin',
    points: 3,
    duration: '4 Hours',
    deps: 'SANC-001, SANC-002, SANC-003',
    apis: ['/api/test/regression', '/api/health'],
    services: ['test-framework', 'ci-pipeline'],
    acceptance: 'Full regression suite passes. Integration tests green. Performance benchmarks met. No regressions on existing payment flows.',
    teams: 'Platform Team',
    statusClass: 'status-todo',
    priorityClass: 'priority-high',
    done: false,
  },
  {
    id: 'SANC-005',
    priority: 'Medium',
    status: 'Complete',
    title: 'Deploy Production Screening Rules',
    owner: 'James O\'Brien',
    points: 2,
    duration: '2 Hours',
    deps: 'SANC-004',
    apis: ['/api/deploy/config', '/api/config/rules'],
    services: ['deployment-pipeline', 'config-service', 'feature-flags'],
    acceptance: 'New rules deployed via feature flags. Canary deployment at 10%. Rollback plan validated. All environments updated.',
    teams: 'Platform Team',
    statusClass: 'status-complete',
    priorityClass: 'priority-medium',
    done: true,
  },
]

const timeline = [
  { label: 'Today', date: '22 Jul', current: true },
  { label: 'Development', date: '22-23 Jul', current: false },
  { label: 'Testing', date: '24 Jul', current: false },
  { label: 'UAT', date: '25 Jul', current: false },
  { label: 'Production', date: '26 Jul', current: false },
]

const risks = [
  { level: 'risk-high', text: 'Regression in existing payment flows if screening logic changes are too broad' },
  { level: 'risk-high', text: 'Performance degradation at peak load with 42+ additional entity checks per transaction' },
  { level: 'risk-medium', text: 'Watchlist sync delay causing gap between regulation effective date and enforcement' },
  { level: 'risk-medium', text: 'UAT environment may not reflect full production entity volume' },
  { level: 'risk-low', text: 'Documentation lag on updated API contracts' },
]

const deployOrder = [
  { name: 'watchlist-manager', color: '#059669' },
  { name: 'data-ingestion-pipeline', color: '#059669' },
  { name: 'sanctions-engine', color: '#2563EB' },
  { name: 'rules-engine', color: '#2563EB' },
  { name: 'payment-gateway', color: '#7C3AED' },
  { name: 'notification-service', color: '#D97706' },
]

const checklist = [
  { label: 'Unit tests passing', done: true },
  { label: 'Integration tests passing', done: true },
  { label: 'Performance benchmarks met (≥10K TPS)', done: true },
  { label: 'Security scan — no new vulnerabilities', done: false },
  { label: 'Regression suite complete', done: false },
  { label: 'Rollback plan reviewed by team', done: false },
  { label: 'UAT sign-off from Compliance', done: false },
  { label: 'Change record approved', done: false },
]

const engRecs = [
  { icon: '⚡', text: 'Prioritize SANC-002 (Watchlist Refresh) — it\'s a dependency blocker for all other stories. Start immediately.' },
  { icon: '🧪', text: 'Set up a shadow testing environment mirroring production entity volumes before running regression.' },
  { icon: '🚦', text: 'Use feature flags for SANC-005 deployment. Canary at 10% before full rollout to minimize blast radius.' },
  { icon: '📊', text: 'Monitor transaction throughput during deployment. If P99 latency exceeds 250ms, trigger rollback automatically.' },
]

export default function Screen3Engineering({ navigate }) {
  const [expandedStory, setExpandedStory] = useState(null)

  const teams = [
    { name: 'Payments Team', pts: 13, color: '#2563EB', stories: 2 },
    { name: 'Trade Finance Team', pts: 5, color: '#7C3AED', stories: 1 },
    { name: 'Compliance Tech', pts: 3, color: '#059669', stories: 1 },
    { name: 'Platform Team', pts: 2, color: '#D97706', stories: 1 },
  ]

  return (
    <div className="s3-root">
      <div className="s3-container">
        {/* Header */}
        <div className="s3-header animate-fadein">
          <div>
            <button className="back-btn" onClick={() => navigate(2)}>← Back to Dashboard</button>
            <h1 className="s3-title">Engineering Impact Analysis</h1>
            <p className="s3-sub">OFAC SDN Update — July 22, 2026 · Epic: Sanctions Update July 2026</p>
          </div>
          <div className="s3-header-kpis">
            {[
              { val: '23', label: 'Story Points' },
              { val: '3', label: 'Teams' },
              { val: '4 Days', label: 'Est. Duration' },
              { val: '5', label: 'Jira Stories' },
            ].map((k, i) => (
              <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 24 }}>
                <div className="s3-kpi">
                  <span className="s3-kpi-val">{k.val}</span>
                  <span className="s3-kpi-label">{k.label}</span>
                </div>
                {i < 3 && <div className="s3-kpi-div" />}
              </div>
            ))}
          </div>
        </div>

        {/* Teams */}
        <div className="teams-row animate-fadein">
          {teams.map((t, i) => (
            <div key={i} className="team-card">
              <div className="team-indicator" style={{ background: t.color }} />
              <div>
                <div className="team-name">{t.name}</div>
                <div className="team-meta">{t.pts} pts · {t.stories} stor{t.stories > 1 ? 'ies' : 'y'}</div>
              </div>
            </div>
          ))}
        </div>

        <div className="s3-grid">
          <div className="s3-main">
            {/* Timeline */}
            <div className="s2-card animate-fadein">
              <div className="card-title" style={{ marginBottom: 20, fontSize: 15, fontWeight: 700, color: 'var(--gray-800)' }}>
                🗓️ Delivery Timeline
              </div>
              <div className="timeline-track">
                {timeline.map((t, i) => (
                  <div key={i} className="timeline-item">
                    {i < timeline.length - 1 && (
                      <div className={`timeline-line ${t.current ? 'line-done' : ''}`} />
                    )}
                    <div className={`timeline-dot ${t.current ? 'current' : 'upcoming'}`} />
                    <div className="timeline-label">{t.label}</div>
                    <div className="timeline-date">{t.date}</div>
                  </div>
                ))}
              </div>
            </div>

            {/* Stories */}
            <div className="s2-card animate-fadein">
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 16 }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                  <span style={{ fontSize: 18 }}>🎯</span>
                  <span className="card-title" style={{ fontSize: 15, fontWeight: 700, color: 'var(--gray-800)' }}>
                    AI Generated Jira Stories
                  </span>
                  <span style={{ fontSize: 11, fontWeight: 700, color: 'var(--purple)', background: 'var(--purple-100)', padding: '3px 10px', borderRadius: 20 }}>✦ AI-Generated</span>
                </div>
                <div style={{ fontSize: 11, color: 'var(--gray-500)', fontWeight: 600 }}>Epic: Sanctions Update July 2026</div>
              </div>
              <div className="stories-list">
                {stories.map((s, i) => (
                  <div key={i} className={`story-card ${s.done ? 'story-done' : ''}`}>
                    <div className="story-top" onClick={() => setExpandedStory(expandedStory === i ? null : i)}>
                      <div className="story-left">
                        <span className="story-id">{s.id}</span>
                        <div className="story-meta-row">
                          <span className={`story-priority ${s.priorityClass}`}>{s.priority}</span>
                          <span className={`story-status ${s.statusClass}`}>{s.status}</span>
                        </div>
                      </div>
                      <div className="story-center">
                        <div className="story-title">{s.title}</div>
                        <div className="story-tags">
                          <span className="story-tag-owner">👤 {s.owner}</span>
                          <span className="story-tag-pts">🎯 {s.points} pts</span>
                          <span className="story-tag-dur">⏱ {s.duration}</span>
                          <span className="story-tag-dep">🔗 Dep: {s.deps}</span>
                        </div>
                      </div>
                      <div className="story-right">
                        <button className="story-expand-btn">
                          {expandedStory === i ? '▲' : '▼'}
                        </button>
                      </div>
                    </div>

                    {expandedStory === i && (
                      <div className="story-details">
                        <div className="detail-grid">
                          <div className="detail-section">
                            <div className="detail-label">Acceptance Criteria</div>
                            <div className="detail-text">{s.acceptance}</div>
                          </div>
                          <div className="detail-section">
                            <div className="detail-label">Affected APIs</div>
                            <div>
                              {s.apis.map(api => (
                                <span key={api} className="api-chip">{api}</span>
                              ))}
                            </div>
                          </div>
                          <div className="detail-section">
                            <div className="detail-label">Affected Microservices</div>
                            <div className="services-row">
                              {s.services.map(svc => (
                                <span key={svc} className="service-chip">{svc}</span>
                              ))}
                            </div>
                          </div>
                          <div style={{ display: 'flex', gap: 24 }}>
                            <div className="detail-section">
                              <div className="detail-label">Assigned Team</div>
                              <div className="detail-text">{s.teams}</div>
                            </div>
                            <div className="detail-section">
                              <div className="detail-label">Est. Duration</div>
                              <div className="detail-text">{s.duration}</div>
                            </div>
                            <div className="detail-section">
                              <div className="detail-label">Story Points</div>
                              <div className="detail-text">{s.points}</div>
                            </div>
                          </div>
                        </div>
                      </div>
                    )}
                  </div>
                ))}
              </div>
            </div>

            {/* Bottom actions */}
            <div className="bottom-actions animate-fadein">
              {[
                { icon: '🎯', label: 'Generate Jira Tickets', color: 'action-color-blue' },
                { icon: '📄', label: 'Download Engineering Plan', color: 'action-color-purple' },
                { icon: '🧪', label: 'Generate Test Cases', color: 'action-color-green' },
                { icon: '🚀', label: 'Create Deployment Checklist', color: 'action-color-amber' },
                { icon: '📊', label: 'Executive Summary', color: 'action-color-red' },
              ].map((a, i) => (
                <button key={i} className={`bottom-action-btn ${a.color}`}>
                  <span>{a.icon}</span> {a.label}
                </button>
              ))}
            </div>
          </div>

          {/* Sidebar */}
          <div className="s3-sidebar">
            {/* AI Engineering Recs */}
            <div className="sidebar-card animate-fadein">
              <div className="sidebar-header">
                <span className="sidebar-icon">🤖</span>
                <span className="sidebar-title">AI Engineering Recommendations</span>
              </div>
              <div className="eng-recs">
                {engRecs.map((r, i) => (
                  <div key={i} className="eng-rec-item">
                    <span className="eng-rec-icon">{r.icon}</span>
                    <span className="eng-rec-text">{r.text}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Risks */}
            <div className="sidebar-card animate-fadein">
              <div className="sidebar-header">
                <span className="sidebar-icon">⚠️</span>
                <span className="sidebar-title">Potential Risks</span>
              </div>
              <div className="risks-list">
                {risks.map((r, i) => (
                  <div key={i} className="risk-item">
                    <span className={`risk-level ${r.level}`}>{r.level === 'risk-high' ? 'High' : r.level === 'risk-medium' ? 'Med' : 'Low'}</span>
                    <span className="risk-text">{r.text}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Rollback */}
            <div className="sidebar-card animate-fadein">
              <div className="sidebar-header">
                <span className="sidebar-icon">↩️</span>
                <span className="sidebar-title">Rollback Strategy</span>
              </div>
              <div className="rollback-content">
                {[
                  'Revert feature flag to disable new screening rules immediately',
                  'Roll back watchlist-manager to previous verified snapshot',
                  'Restore payment-gateway config from T-1 backup',
                  'Notify Compliance Team and pause affected transactions',
                  'Run integrity check on entity database post-rollback',
                ].map((s, i) => (
                  <div key={i} className="rollback-item">
                    <span className="rollback-step">{i + 1}</span>
                    <span className="rollback-text">{s}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Deploy Order */}
            <div className="sidebar-card animate-fadein">
              <div className="sidebar-header">
                <span className="sidebar-icon">🚀</span>
                <span className="sidebar-title">Deployment Order</span>
              </div>
              <div className="deploy-order">
                {deployOrder.map((d, i) => (
                  <div key={i} className="deploy-item">
                    <span className="deploy-num" style={{ background: `${d.color}15`, color: d.color, borderColor: `${d.color}30` }}>{i + 1}</span>
                    <span className="deploy-name" style={{ color: d.color }}>{d.name}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Testing Checklist */}
            <div className="sidebar-card animate-fadein">
              <div className="sidebar-header">
                <span className="sidebar-icon">✅</span>
                <span className="sidebar-title">Testing Checklist</span>
              </div>
              <div className="checklist">
                {checklist.map((c, i) => (
                  <div key={i} className="checklist-item">
                    <div className={`check-box ${c.done ? 'checked' : ''}`}>
                      {c.done && (
                        <svg width="10" height="10" viewBox="0 0 10 10" fill="none">
                          <polyline points="2 5 4.5 7.5 8 3" stroke="white" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
                        </svg>
                      )}
                    </div>
                    <span className={`check-label ${c.done ? 'done-label' : ''}`}>{c.label}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
