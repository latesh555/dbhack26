import { useState } from 'react'
import './Screen2Dashboard.css'

const HEATMAP_ROWS = ['Compliance', 'Payments', 'Trade Finance', 'Operations', 'Technology', 'AML']
const HEATMAP_COLS = ['Low', 'Medium', 'High', 'Critical']

const heatmapData = [
  ['#DCFCE7:#059669:L', '#FEF3C7:#D97706:M', '#FEE2E2:#DC2626:H', '#FEE2E2:#991B1B:C'],
  ['#FEF3C7:#D97706:M', '#FEE2E2:#DC2626:H', '#FEE2E2:#991B1B:C', '#FEE2E2:#991B1B:C'],
  ['#DCFCE7:#059669:L', '#FEF3C7:#D97706:M', '#FEE2E2:#DC2626:H', '#FEE2E2:#DC2626:H'],
  ['#FEF3C7:#D97706:M', '#FEE2E2:#DC2626:H', '#FEE2E2:#DC2626:H', '#FEE2E2:#991B1B:C'],
  ['#DCFCE7:#059669:L', '#DCFCE7:#059669:L', '#FEF3C7:#D97706:M', '#FEE2E2:#DC2626:H'],
  ['#FEF3C7:#D97706:M', '#FEE2E2:#DC2626:H', '#FEE2E2:#991B1B:C', '#FEE2E2:#991B1B:C'],
]

const heatmapLabels = [
  [['Policy Breach', '2'], ['Watchlist Gap', '8'], ['Screening Miss', '14'], ['Regulatory Fine', '22']],
  [['Slow Settle', '3'], ['Block Risk', '11'], ['Disruption', '18'], ['SWIFT Halt', '29']],
  [['Doc Delay', '1'], ['Deal Pause', '6'], ['Finance Risk', '15'], ['Deal Loss', '18']],
  [['Manual Work', '4'], ['Process Gap', '9'], ['Ops Breach', '17'], ['SLA Breach', '24']],
  [['Minor Bug', '2'], ['API Change', '7'], ['System Risk', '12'], ['Outage', '19']],
  [['Alert Low', '3'], ['AML Gap', '10'], ['STR Miss', '18'], ['Fine Risk', '26']],
]

const recommendations = [
  { priority: 1, text: 'Refresh sanctions watchlist immediately.', confidence: 98, color: 'red', bar: '#DC2626' },
  { priority: 2, text: 'Pause transactions involving newly sanctioned entities.', confidence: 96, color: 'red', bar: '#DC2626' },
  { priority: 3, text: 'Run retrospective screening for the last 30 days.', confidence: 91, color: 'amber', bar: '#D97706' },
  { priority: 4, text: 'Notify Trade Finance Team immediately.', confidence: 89, color: 'amber', bar: '#D97706' },
  { priority: 5, text: 'Deploy updated screening rules within 24 hours.', confidence: 85, color: 'blue', bar: '#2563EB' },
]

const actions = [
  { emoji: '🎯', label: 'Generate Jira Stories', desc: 'Auto-create sprint tickets', color: 'action-color-blue' },
  { emoji: '📊', label: 'Executive Report', desc: 'C-suite ready PDF', color: 'action-color-purple' },
  { emoji: '📧', label: 'Compliance Email', desc: 'Draft team notification', color: 'action-color-green' },
  { emoji: '📑', label: 'PowerPoint Deck', desc: 'Board presentation', color: 'action-color-amber' },
  { emoji: '💾', label: 'Export Analysis', desc: 'JSON / CSV / PDF', color: 'action-color-blue' },
]

const activities = [
  { type: 'activity-ai', text: 'AI completed impact analysis — 98% confidence', time: '2 mins ago' },
  { type: 'activity-system', text: 'Sanctions watchlist cross-referenced (42 entities)', time: '3 mins ago' },
  { type: 'activity-upload', text: 'Document uploaded — OFAC SDN Update July 2026', time: '5 mins ago' },
  { type: 'activity-complete', text: 'Engineering impact mapped — 23 story points', time: '7 mins ago' },
]

export default function Screen2Dashboard({ navigate }) {
  const [engExpanded, setEngExpanded] = useState(false)

  return (
    <div className="s2-root">
      <div className="s2-container">
        {/* Banner */}
        <div className="s2-banner animate-fadein">
          <div className="banner-left">
            <div className="banner-type-badge">⚠️ OFAC Sanctions Update — CRITICAL</div>
            <div className="banner-title">OFAC SDN Update — July 22, 2026</div>
            <div className="banner-summary">
              <span className="summary-sparkle">✦</span>
              <strong>AI Summary:</strong> "This update introduces <strong>42 newly sanctioned entities</strong>, expands restrictions on the energy sector, and requires immediate updates to transaction screening rules. Immediate remediation required across 12 enterprise applications."
            </div>
            <div className="banner-meta">
              {[
                { label: 'Document Type', value: 'OFAC Sanctions Update', cls: '' },
                { label: 'Published', value: '22 July 2026', cls: '' },
                { label: 'Effective', value: 'Immediate', cls: 'effective' },
                { label: 'Deadline', value: '31 Oct 2026', cls: '' },
                { label: 'Severity', value: '🔴 Critical', cls: 'critical' },
              ].map((m, i) => (
                <div key={i} className="meta-item">
                  <span className="meta-label">{m.label}</span>
                  <span className={`meta-value ${m.cls}`}>{m.value}</span>
                </div>
              ))}
            </div>
          </div>
          <div className="banner-right">
            <ConfidenceDial value={98} />
          </div>
        </div>

        <div className="s2-grid">
          <div className="s2-main">
            {/* Section 1 — Enterprise Impact */}
            <div className="s2-card animate-fadein">
              <div className="card-header">
                <div className="card-title-row">
                  <span className="card-icon">🏢</span>
                  <h2 className="card-title">Enterprise Impact Metrics</h2>
                  <span className="card-badge critical-badge">Critical</span>
                </div>
              </div>
              <div className="impact-grid">
                {[
                  { icon: '💻', label: 'Applications Impacted', val: 20, color: 'color-blue' },
                  { icon: '📉', label: 'Trade Finance', val: 'High', color: 'color-red' },
                  { icon: '📑', label: 'SWIFT Gateway', val: 'Low', color: 'color-green' },
                  { icon: '💳', label: 'AML Monitoring', val: 'Low', color: 'color-green' },
                  { icon: '🧾', label: 'Trade Settlement', val: 8, color: 'color-purple' },
                  { icon: '💱', label: 'Payment Flows', val: 55, color: 'color-blue' },
                  { icon: '🏦', label: 'Readiness Score', val: '68%', color: 'color-amber' },
                  { icon: '💶', label: 'Compliance Cost', val: '€348', color: 'color-amber' },
                ].map((c, i) => (
                  <div key={i} className={`impact-chip ${c.color}`}>
                    <span className="impact-chip-icon">{c.icon}</span>
                    <span className="impact-chip-val">{c.val}</span>
                    <span className="impact-chip-label">{c.label}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Section 2 — Customer & Transaction Impact */}
            <div className="s2-card animate-fadein">
              <div className="card-header">
                <div className="card-title-row">
                  <span className="card-icon">💳</span>
                  <h2 className="card-title">Customer & Transaction Impact</h2>
                  <span className="card-badge critical-badge">186 Pending</span>
                </div>
              </div>
              <div className="kpi-grid">
                {[
                  { icon: '👤', label: 'Potential Customer Matches', val: '128', trend: '▲ 42', color: 'color-red' },
                  { icon: '⏳', label: 'Pending Payments', val: '186', trend: '⚠ Hold', color: 'color-amber' },
                  { icon: '📦', label: 'Trade Finance Deals', val: '41', trend: 'Review', color: 'color-amber' },
                  { icon: '🏦', label: 'Corporate Accounts', val: '22', trend: 'Flag', color: 'color-red' },
                  { icon: '⚡', label: 'SWIFT Messages', val: '94', trend: 'Block', color: 'color-blue' },
                  { icon: '🔄', label: 'Transactions to Rescreen', val: '38,241', trend: 'Queue', color: 'color-purple' },
                ].map((k, i) => (
                  <div key={i} className="kpi-card">
                    <div className="kpi-top">
                      <span className="kpi-icon">{k.icon}</span>
                      <span className={`kpi-trend ${k.color}`}>{k.trend}</span>
                    </div>
                    <div className={`kpi-value ${k.color}`}>{k.val}</div>
                    <div className="kpi-label">{k.label}</div>
                  </div>
                ))}
              </div>
              <div style={{ marginTop: 20 }}>
                <TrendChart />
              </div>
            </div>

            {/* Section 3 — Business Impact */}
            <div className="s2-card animate-fadein">
              <div className="card-header">
                <div className="card-title-row">
                  <span className="card-icon">💰</span>
                  <h2 className="card-title">Business Impact</h2>
                  <span className="card-badge critical-badge">Critical</span>
                </div>
              </div>
              <div className="biz-kpi-grid">
                {[
                  { icon: '💵', label: 'Revenue at Risk', val: '$145K/day', sub: 'Est. daily exposure', cls: 'biz-bad', valCls: 'biz-val-red' },
                  { icon: '⏱️', label: 'Settlement Delay', val: 'High', sub: 'T+2 → T+5 risk', cls: 'biz-warn', valCls: 'biz-val-amber' },
                  { icon: '📋', label: 'Operational Reviews', val: '620', sub: 'Manual reviews queued', cls: '', valCls: '' },
                  { icon: '🕐', label: 'Investigation Hours', val: '190', sub: 'Est. FTE hours', cls: 'biz-warn', valCls: 'biz-val-amber' },
                  { icon: '🔴', label: 'Business Criticality', val: 'Critical', sub: 'Board escalation needed', cls: 'biz-bad', valCls: 'biz-val-red' },
                  { icon: '💳', label: 'Payment Disruption', val: '18%', sub: 'Of daily payment volume', cls: 'biz-warn', valCls: 'biz-val-amber' },
                ].map((b, i) => (
                  <div key={i} className={`biz-kpi-card ${b.cls}`}>
                    <div className="biz-kpi-icon">{b.icon}</div>
                    <div className={`biz-kpi-value ${b.valCls}`}>{b.val}</div>
                    <div className="biz-kpi-label">{b.label}</div>
                    <div className="biz-kpi-sub">{b.sub}</div>
                  </div>
                ))}
              </div>
              <div style={{ marginTop: 20 }}>
                <ImpactScoreMeter score={84} />
              </div>
            </div>

            {/* Section 4 — Engineering Impact */}
            <div className="s2-card animate-fadein">
              <div className="card-header">
                <div className="card-title-row">
                  <span className="card-icon">⚙️</span>
                  <h2 className="card-title">Engineering Impact</h2>
                  <span className="card-badge blue-badge">23 Story Points</span>
                </div>
              </div>
              <div className="eng-summary">
                {[
                  { val: '23', label: 'Story Points' },
                  { val: '3', label: 'Teams' },
                  { val: '4 Days', label: 'Est. Duration' },
                  { val: '5', label: 'Jira Stories' },
                ].map((e, i) => (
                  <div key={i} className="eng-kpi">
                    <span className="eng-kpi-val">{e.val}</span>
                    <span className="eng-kpi-label">{e.label}</span>
                  </div>
                ))}
              </div>
              {engExpanded && (
                <div className="eng-expanded">
                  <div className="eng-teams">
                    {['Payments Team', 'Trade Finance Team', 'Compliance Tech', 'Platform Team'].map(t => (
                      <span key={t} className="team-tag">{t}</span>
                    ))}
                  </div>
                </div>
              )}
              <button className="view-eng-btn" onClick={() => navigate(3)}>
                View Engineering Details →
              </button>
            </div>

            {/* Section 5 — Risk Heatmap */}
            <div className="s2-card animate-fadein">
              <div className="card-header">
                <div className="card-title-row">
                  <span className="card-icon">🔥</span>
                  <h2 className="card-title">Risk Heatmap</h2>
                  <span className="card-badge critical-badge" style={{ fontSize: 12, padding: '4px 12px' }}>Risk Score: 94/100 Critical</span>
                </div>
              </div>
              <div className="heatmap-wrap">
                <div className="heatmap-table">
                  <div className="heatmap-row header-row">
                    <div className="heatmap-cell row-label col-header">Domain</div>
                    {HEATMAP_COLS.map(c => (
                      <div key={c} className="heatmap-cell col-header">{c}</div>
                    ))}
                  </div>
                  {HEATMAP_ROWS.map((row, ri) => (
                    <div key={ri} className="heatmap-row">
                      <div className="heatmap-cell row-label">{row}</div>
                      {heatmapData[ri].map((cell, ci) => {
                        const [bg, fg, abbr] = cell.split(':')
                        const { label, count } = { label: heatmapLabels[ri][ci][0], count: heatmapLabels[ri][ci][1] }
                        return (
                          <div
                            key={ci}
                            className="heatmap-cell data-cell"
                            style={{ background: bg, color: fg }}
                            title={`${row} × ${HEATMAP_COLS[ci]}: ${label} (${count} issues)`}
                          >
                            <div style={{ fontSize: 11, fontWeight: 700 }}>{label}</div>
                            <div style={{ fontSize: 10, opacity: 0.8 }}>{count} issues</div>
                          </div>
                        )
                      })}
                    </div>
                  ))}
                </div>
                <div className="heatmap-legend">
                  <span style={{ fontSize: 11, color: 'var(--gray-500)', fontWeight: 600 }}>Risk Level:</span>
                  {[
                    { bg: '#DCFCE7', color: '#059669', label: 'Low' },
                    { bg: '#FEF3C7', color: '#D97706', label: 'Medium' },
                    { bg: '#FEE2E2', color: '#DC2626', label: 'High' },
                    { bg: '#FEE2E2', color: '#991B1B', label: 'Critical' },
                  ].map(l => (
                    <div key={l.label} className="legend-item">
                      <div className="legend-dot" style={{ background: l.bg, border: `1px solid ${l.color}` }} />
                      <span style={{ fontSize: 11, color: 'var(--gray-600)', fontWeight: 500 }}>{l.label}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>

            
          </div>

          {/* Sidebar */}
          <div className="s2-sidebar">
            
{/* Section 6 — Recommended Actions */}
            <div className="s2-card animate-fadein">
              <div className="card-header">
                <div className="card-title-row">
                  <span className="card-icon">⚡</span>
                  <h2 className="card-title">Recommended Actions</h2>
                </div>
              </div>
              <div className="actions-grid">
                {actions.map((a, i) => (
                  <div key={i} className={`action-card ${a.color}`}>
                    <div className="action-icon-row">
                      <span className="action-emoji">{a.emoji}</span>
                      <span className="action-ai-sparkle">✦</span>
                    </div>
                    <div className="action-label">{a.label}</div>
                    <div className="action-desc">{a.desc}</div>
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

function ConfidenceDial({ value }) {
  const angle = (value / 100) * 180
  return (
    <div style={{ textAlign: 'center' }}>
      <svg width="140" height="90" viewBox="0 0 140 80">
        <path d="M 15 70 A 55 55 0 0 1 125 70" fill="none" stroke="rgba(255,255,255,0.1)" strokeWidth="12" strokeLinecap="round" />
        <path d="M 15 70 A 55 55 0 0 1 125 70" fill="none" stroke="url(#dialGrad)" strokeWidth="12" strokeLinecap="round"
          strokeDasharray={`${(value / 100) * 172.8} 172.8`} />
        <defs>
          <linearGradient id="dialGrad" x1="0" y1="0" x2="1" y2="0">
            <stop offset="0%" stopColor="#10B981" />
            <stop offset="60%" stopColor="#F59E0B" />
            <stop offset="100%" stopColor="#EF4444" />
          </linearGradient>
        </defs>
        <text x="70" y="68" textAnchor="middle" fill="white" fontSize="22" fontWeight="800">{value}%</text>
        <text x="70" y="82" textAnchor="middle" fill="rgba(255,255,255,0.5)" fontSize="10">AI Confidence</text>
      </svg>
    </div>
  )
}

function TrendChart() {
  const points = [20, 35, 45, 80, 90, 150, 186]
  const maxVal = 200
  const w = 540, h = 80
  const ptsStr = points.map((v, i) => `${(i / (points.length - 1)) * w},${h - (v / maxVal) * h}`).join(' ')
  const areaStr = `0,${h} ${ptsStr} ${w},${h}`

  return (
    <div style={{ padding: '12px 0 0' }}>
      <div style={{ fontSize: 12, fontWeight: 700, color: 'var(--gray-700)', marginBottom: 8 }}>Impacted Transactions Over 7 Days</div>
      <svg viewBox={`0 0 ${w} ${h}`} style={{ width: '100%', height: 80, borderRadius: 6 }}>
        <defs>
          <linearGradient id="chartGrad" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%" stopColor="#EF4444" stopOpacity="0.2" />
            <stop offset="100%" stopColor="#EF4444" stopOpacity="0" />
          </linearGradient>
        </defs>
        <polygon points={areaStr} fill="url(#chartGrad)" />
        <polyline points={ptsStr} fill="none" stroke="#EF4444" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" />
        {points.map((v, i) => (
          <circle key={i} cx={(i / (points.length - 1)) * w} cy={h - (v / maxVal) * h} r="4" fill="#EF4444" />
        ))}
      </svg>
    </div>
  )
}

function ImpactScoreMeter({ score }) {
  return (
    <div style={{ padding: '12px 0 0' }}>
      <div style={{ fontSize: 12, fontWeight: 700, color: 'var(--gray-700)', marginBottom: 8, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <span>Business Impact Score</span>
        <span style={{ color: '#DC2626', fontWeight: 800 }}>{score}/100 — Critical</span>
      </div>
      <div style={{ height: 14, background: 'linear-gradient(to right, #DCFCE7, #FEF3C7, #FEE2E2, #991B1B)', borderRadius: 7, position: 'relative', overflow: 'visible' }}>
        <div style={{
          position: 'absolute',
          left: `${score}%`,
          top: '50%',
          transform: 'translate(-50%, -50%)',
          width: 22,
          height: 22,
          background: 'white',
          border: '3px solid #DC2626',
          borderRadius: '50%',
          boxShadow: '0 2px 8px rgba(220,38,38,0.4)',
          zIndex: 2
        }} />
      </div>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 6 }}>
        {['Low', 'Medium', 'High', 'Critical'].map(l => (
          <span key={l} style={{ fontSize: 10, color: 'var(--gray-500)' }}>{l}</span>
        ))}
      </div>
    </div>
  )
}

function ArchGraph() {
  const nodes = [
    { label: 'OFAC\nRegulation', x: 50, y: 50, color: '#DC2626', border: '#FECACA', lg: true },
    { label: 'Sanctions\nEngine', x: 25, y: 30, color: '#2563EB', border: '#BFDBFE', lg: false },
    { label: 'Payment\nService', x: 75, y: 30, color: '#7C3AED', border: '#EDE9FE', lg: false },
    { label: 'Trade\nFinance', x: 15, y: 65, color: '#059669', border: '#A7F3D0', lg: false },
    { label: 'KYC/AML\nAPI', x: 40, y: 75, color: '#D97706', border: '#FDE68A', lg: false },
    { label: 'Core\nBanking', x: 65, y: 75, color: '#0891B2', border: '#A5F3FC', lg: false },
    { label: 'SWIFT\nGateway', x: 85, y: 60, color: '#7C3AED', border: '#EDE9FE', lg: false },
  ]
  const edges = [[0,1],[0,2],[1,3],[1,4],[2,6],[0,5],[5,4],[5,6]]

  return (
    <svg viewBox="0 0 100 100" style={{ width: '100%', height: '100%', padding: 4 }}>
      {edges.map(([a, b], i) => (
        <line
          key={i}
          x1={`${nodes[a].x}%`} y1={`${nodes[a].y}%`}
          x2={`${nodes[b].x}%`} y2={`${nodes[b].y}%`}
          stroke="var(--gray-300)" strokeWidth="0.5" strokeDasharray="2 2"
        />
      ))}
      {nodes.map((n, i) => (
        <g key={i}>
          <rect
            x={`${n.x - 7}%`} y={`${n.y - 5}%`}
            width="14%" height="10%"
            rx="1.5" fill="white" stroke={n.border} strokeWidth="0.7"
          />
          {n.label.split('\n').map((line, li) => (
            <text key={li} x={`${n.x}%`} y={`${n.y + (li * 3) - 1}%`}
              textAnchor="middle" fontSize="2.8" fontWeight="700" fill={n.color}
            >
              {line}
            </text>
          ))}
        </g>
      ))}
    </svg>
  )
}
