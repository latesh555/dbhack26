import { useState } from 'react'
import './Header.css'

export default function Header({ navigate, screen }) {
  const [searchVal, setSearchVal] = useState('')

  return (
    <header className="header">
      <div className="header-inner">
        {/* Logo */}
        <div className="header-logo" onClick={() => navigate(1)}>
          <div className="logo-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M12 2L2 7l10 5 10-5-10-5z" fill="white"/>
              <path d="M2 17l10 5 10-5" stroke="white" strokeWidth="2" strokeLinecap="round"/>
              <path d="M2 12l10 5 10-5" stroke="white" strokeWidth="2" strokeLinecap="round"/>
            </svg>
          </div>
          <div>
            <span className="logo-name">RegIntel AI</span>
            <span className="logo-tag">Enterprise</span>
          </div>
        </div>

        {/* Search */}
        <div className="header-search">
          <svg className="search-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/>
          </svg>
          <input
            type="text"
            placeholder="Search regulations, documents, sanctions..."
            value={searchVal}
            onChange={e => setSearchVal(e.target.value)}
            className="search-input"
          />
          <kbd className="search-kbd">⌘K</kbd>
        </div>

        {/* Right actions */}
        <div className="header-actions">
          {/* Nav tabs */}
          <nav className="header-nav">
            <button className={`nav-tab ${screen === 2 ? 'active' : ''}`} onClick={() => navigate(2)}>
              Dashboard
            </button>
            <button className={`nav-tab ${screen === 3 ? 'active' : ''}`} onClick={() => navigate(3)}>
              Engineering
            </button>
          </nav>

          {/* Badge */}
          <div className="notif-btn">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/>
              <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
            </svg>
            <span className="notif-dot"></span>
          </div>

          <div className="avatar-btn">
            <span>JD</span>
          </div>
        </div>
      </div>
    </header>
  )
}
