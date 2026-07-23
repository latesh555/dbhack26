import { useState } from 'react'
import './Screen1Upload.css'

const recentUploads = [
  { name: 'OFAC SDN Update July 2026', type: 'Sanctions', severity: 'Critical', time: '2h ago', icon: '🏦' },
  { name: 'RBI KYC Circular No. 42/2026', type: 'Regulatory', severity: 'High', time: '1d ago', icon: '📋' },
  { name: 'FATF AML Guidance 2026', type: 'AML Policy', severity: 'Medium', time: '3d ago', icon: '🔍' },
  { name: 'ECB Reporting Update Q3', type: 'Reporting', severity: 'Low', time: '5d ago', icon: '📊' },
]

const fileTypes = [
  { ext: 'PDF', color: '#DC2626', icon: (
    <svg viewBox="0 0 24 24" fill="none" width="24" height="24">
      <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" stroke="#DC2626" strokeWidth="1.5" fill="none"/>
      <polyline points="14 2 14 8 20 8" stroke="#DC2626" strokeWidth="1.5"/>
      <line x1="9" y1="15" x2="15" y2="15" stroke="#DC2626" strokeWidth="1.5"/>
      <line x1="9" y1="11" x2="15" y2="11" stroke="#DC2626" strokeWidth="1.5"/>
    </svg>
  )},
  { ext: 'DOCX', color: '#2563EB', icon: (
    <svg viewBox="0 0 24 24" fill="none" width="24" height="24">
      <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" stroke="#2563EB" strokeWidth="1.5" fill="none"/>
      <polyline points="14 2 14 8 20 8" stroke="#2563EB" strokeWidth="1.5"/>
      <line x1="9" y1="15" x2="15" y2="15" stroke="#2563EB" strokeWidth="1.5"/>
    </svg>
  )},
  { ext: 'CSV', color: '#059669', icon: (
    <svg viewBox="0 0 24 24" fill="none" width="24" height="24">
      <rect x="3" y="3" width="18" height="18" rx="2" stroke="#059669" strokeWidth="1.5"/>
      <line x1="3" y1="9" x2="21" y2="9" stroke="#059669" strokeWidth="1.5"/>
      <line x1="3" y1="15" x2="21" y2="15" stroke="#059669" strokeWidth="1.5"/>
      <line x1="9" y1="3" x2="9" y2="21" stroke="#059669" strokeWidth="1.5"/>
    </svg>
  )},
  { ext: 'XML', color: '#7C3AED', icon: (
    <svg viewBox="0 0 24 24" fill="none" width="24" height="24">
      <polyline points="16 18 22 12 16 6" stroke="#7C3AED" strokeWidth="1.5"/>
      <polyline points="8 6 2 12 8 18" stroke="#7C3AED" strokeWidth="1.5"/>
    </svg>
  )},
  { ext: 'JSON', color: '#D97706', icon: (
    <svg viewBox="0 0 24 24" fill="none" width="24" height="24">
      <path d="M5 3a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V5a2 2 0 0 0-2-2H5z" stroke="#D97706" strokeWidth="1.5"/>
      <path d="M9 8l-2 4 2 4M15 8l2 4-2 4" stroke="#D97706" strokeWidth="1.5" strokeLinecap="round"/>
    </svg>
  )},
]

const severityClass = { Critical: 'severity-critical', High: 'severity-high', Medium: 'severity-medium', Low: 'severity-low' }

export default function Screen1Upload({ navigate }) {
  const [dragging, setDragging] = useState(false)
  const [url, setUrl] = useState('')
  const [analyzing, setAnalyzing] = useState(false)

  const handleAnalyze = () => {
    setAnalyzing(true)
    setTimeout(() => {
      setAnalyzing(false)
      navigate(2)
    }, 2000)
  }

  return (
    <div className="s1-root">
      {/* Top bar */}
      <div className="s1-topbar">
        <div className="s1-logo">
          <div className="s1-logo-icon">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
              <path d="M12 2L2 7l10 5 10-5-10-5z" fill="white"/>
              <path d="M2 17l10 5 10-5" stroke="white" strokeWidth="2" strokeLinecap="round"/>
              <path d="M2 12l10 5 10-5" stroke="white" strokeWidth="2" strokeLinecap="round"/>
            </svg>
          </div>
          <span className="s1-logo-text">RegIntel AI</span>
          <span className="s1-logo-badge">Enterprise</span>
        </div>
        <div className="s1-topbar-right">
          <span className="s1-topbar-item">Documentation</span>
          <div className="s1-topbar-avatar">SD</div>
        </div>
      </div>

      <div className="s1-container">
        {/* Left column — upload */}
        <div className="s1-left">
          {/* Hero */}
          <div className="s1-hero animate-fadein">
            <h1 className="s1-title">RegIntel AI</h1>
            <p className="s1-subtitle">
              From regulatory update to business impact in under <strong>5 minutes</strong>.
            </p>
          </div>

          {/* Upload area */}
          <div
            className={`s1-dropzone ${dragging ? 'dragging' : ''}`}
            onDragOver={e => { e.preventDefault(); setDragging(true) }}
            onDragLeave={() => setDragging(false)}
            onDrop={e => { e.preventDefault(); setDragging(false); handleAnalyze() }}
            onClick={handleAnalyze}
          >
            <div className="dropzone-icon">
              <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
                <polyline points="17 8 12 3 7 8"/>
                <line x1="12" y1="3" x2="12" y2="15"/>
              </svg>
            </div>
            <h3 className="dropzone-title">Drop your document here</h3>
            <p className="dropzone-sub">or click to browse files</p>
            <div className="dropzone-formats">
              {fileTypes.map(f => (
                <div key={f.ext} className="format-chip">
                  {f.icon}
                  <span style={{ color: f.color, fontWeight: 600, fontSize: 12 }}>{f.ext}</span>
                </div>
              ))}
            </div>
            <p className="dropzone-hint">Maximum file size: 50MB · Encrypted in transit</p>
          </div>

          {/* OR URL */}
          <div className="s1-or">
            <div className="or-line"></div>
            <span className="or-text">OR</span>
            <div className="or-line"></div>
          </div>

          <div className="s1-url-row">
            <div className="url-input-wrap">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="var(--gray-400)" strokeWidth="2" style={{flexShrink:0}}>
                <path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"/>
                <path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"/>
              </svg>
              <input
                type="url"
                placeholder="Paste document URL (e.g. https://ofac.treasury.gov/...)"
                value={url}
                onChange={e => setUrl(e.target.value)}
                className="url-input"
              />
            </div>
          </div>

          {/* CTA */}
          <button
            className={`s1-cta ${analyzing ? 'loading' : ''}`}
            onClick={handleAnalyze}
            disabled={analyzing}
          >
            {analyzing ? (
              <>
                <div className="cta-spinner"></div>
                Analyzing with AI...
              </>
            ) : (
              <>
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
                </svg>
                Analyze Document
              </>
            )}
          </button>
        </div>

        {/* Right column */}
        <div className="s1-right animate-fadein">
          {/* AI illustration panel */}
          {/* <div className="s1-illustration">
            <div className="illustration-header">
              <div className="illus-dot red"></div>
              <div className="illus-dot amber"></div>
              <div className="illus-dot green"></div>
              <span className="illus-title">AI Analysis Engine</span>
            </div>
            <div className="illus-body">
              <AIIllustration />
            </div>
          </div> */}

          {/* Recent uploads */}
          <div className="s1-recent-card">
            <div className="recent-header">
              <h3 className="recent-title">Recent Uploads</h3>
              <span className="recent-see-all">See all →</span>
            </div>
            <div className="recent-list">
              {recentUploads.map((u, i) => (
                <div key={i} className="recent-item" onClick={() => navigate(2)}>
                  <div className="recent-icon">{u.icon}</div>
                  <div className="recent-info">
                    <div className="recent-name">{u.name}</div>
                    <div className="recent-meta">
                      <span className="recent-type">{u.type}</span>
                      <span className={`severity-badge ${severityClass[u.severity]}`}>{u.severity}</span>
                    </div>
                  </div>
                  <span className="recent-time">{u.time}</span>
                </div>
              ))}
            </div>
          </div>

          {/* Stats */}
          <div className="s1-stats">
            <div className="stat-card">
              <div className="stat-num">2,847</div>
              <div className="stat-label">Documents Analyzed</div>
            </div>
            <div className="stat-card">
              <div className="stat-num">99.8%</div>
              <div className="stat-label">AI Accuracy</div>
            </div>
            <div className="stat-card">
              <div className="stat-num">&lt;4 min</div>
              <div className="stat-label">Avg. Analysis Time</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

function AIIllustration() {
  return (
    <div className="ai-illus">
      {/* Central brain */}
      <div className="ai-center">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
          <path d="M12 2a4 4 0 0 1 4 4v1a4 4 0 0 1-4 4 4 4 0 0 1-4-4V6a4 4 0 0 1 4-4z" fill="url(#grad1)" opacity="0.8"/>
          <path d="M9.5 11.5c-2 0-4 1.5-4 3.5v1c0 2 1.5 4 4 4h5c2.5 0 4-2 4-4v-1c0-2-2-3.5-4-3.5" stroke="url(#grad1)" strokeWidth="1.5" fill="none"/>
          <defs>
            <linearGradient id="grad1" x1="0" y1="0" x2="1" y2="1">
              <stop offset="0%" stopColor="#2563EB"/>
              <stop offset="100%" stopColor="#7C3AED"/>
            </linearGradient>
          </defs>
        </svg>
        <span className="ai-label">AI Engine</span>
      </div>

      {/* Connected nodes */}
      <div className="ai-nodes">
        {['Regulation\nParser', 'Risk\nAnalyzer', 'Impact\nMapper', 'Report\nGenerator', 'Sanctions\nChecker', 'Compliance\nAdvisor'].map((label, i) => {
          const angle = (i / 6) * 2 * Math.PI - Math.PI / 2
          const radius = 90
          const x = 50 + radius * Math.cos(angle)
          const y = 50 + radius * Math.sin(angle)
          const colors = ['#2563EB','#7C3AED','#059669','#D97706','#DC2626','#0891B2']
          return (
            <div key={i} className="ai-node" style={{ left: `${x}%`, top: `${y}%`, borderColor: colors[i], color: colors[i] }}>
              {label.split('\n').map((l, j) => <span key={j} style={{ display: 'block', fontSize: 9, lineHeight: 1.2 }}>{l}</span>)}
            </div>
          )
        })}
      </div>

      {/* Pulse ring */}
      <div className="ai-pulse-ring"></div>
    </div>
  )
}
