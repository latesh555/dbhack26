import './HeroIllustration.css';

export default function HeroIllustration() {
  return (
    <div className="hero-illustration">
      <svg viewBox="0 0 400 360" className="hero-illustration__svg">
        <defs>
          <linearGradient id="hero-bg" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="#0066ff" stopOpacity="0.15" />
            <stop offset="100%" stopColor="#8b5cf6" stopOpacity="0.1" />
          </linearGradient>
          <linearGradient id="hero-accent" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="0%" stopColor="#0066ff" />
            <stop offset="100%" stopColor="#8b5cf6" />
          </linearGradient>
          <filter id="glow">
            <feGaussianBlur stdDeviation="3" result="coloredBlur" />
            <feMerge>
              <feMergeNode in="coloredBlur" />
              <feMergeNode in="SourceGraphic" />
            </feMerge>
          </filter>
        </defs>

        {/* Background circle */}
        <circle cx="200" cy="180" r="140" fill="url(#hero-bg)" />

        {/* Central AI brain node */}
        <circle cx="200" cy="160" r="36" fill="#1a2438" stroke="url(#hero-accent)" strokeWidth="2" filter="url(#glow)" />
        <text x="200" y="155" textAnchor="middle" fill="#8b5cf6" fontSize="10" fontWeight="700">AI</text>
        <text x="200" y="168" textAnchor="middle" fill="#94a3b8" fontSize="7">ENGINE</text>

        {/* Document node */}
        <rect x="60" y="120" width="56" height="72" rx="6" fill="#141c2e" stroke="#3b82f6" strokeWidth="1.5" />
        <line x1="72" y1="140" x2="104" y2="140" stroke="#64748b" strokeWidth="1.5" />
        <line x1="72" y1="152" x2="104" y2="152" stroke="#64748b" strokeWidth="1.5" />
        <line x1="72" y1="164" x2="96" y2="164" stroke="#64748b" strokeWidth="1.5" />
        <text x="88" y="108" textAnchor="middle" fill="#3b82f6" fontSize="8" fontWeight="600">OFAC</text>

        {/* Enterprise systems */}
        {[
          { x: 300, y: 100, label: 'Payments', color: '#10b981' },
          { x: 320, y: 180, label: 'Screening', color: '#f59e0b' },
          { x: 300, y: 260, label: 'Trade', color: '#06b6d4' },
          { x: 80, y: 260, label: 'Compliance', color: '#8b5cf6' },
          { x: 60, y: 180, label: 'Risk', color: '#ef4444' },
        ].map(({ x, y, label, color }) => (
          <g key={label}>
            <rect x={x - 32} y={y - 16} width="64" height="32" rx="8" fill="#141c2e" stroke={color} strokeWidth="1" opacity="0.9" />
            <text x={x} y={y + 4} textAnchor="middle" fill={color} fontSize="8" fontWeight="600">{label}</text>
          </g>
        ))}

        {/* Connection lines */}
        {[
          [116, 156, 164, 160],
          [236, 160, 268, 116],
          [236, 165, 288, 180],
          [236, 170, 268, 260],
          [164, 175, 112, 260],
          [164, 168, 92, 180],
        ].map(([x1, y1, x2, y2], i) => (
          <line
            key={i}
            x1={x1} y1={y1} x2={x2} y2={y2}
            stroke="url(#hero-accent)"
            strokeWidth="1"
            strokeDasharray="4 3"
            opacity="0.5"
          />
        ))}

        {/* Data flow particles */}
        {[0, 1, 2].map((i) => (
          <circle key={i} cx={140 + i * 30} cy={158} r="2" fill="#0066ff" opacity="0.8">
            <animate attributeName="cx" from="116" to="164" dur="2s" repeatCount="indefinite" begin={`${i * 0.6}s`} />
            <animate attributeName="opacity" values="0;1;0" dur="2s" repeatCount="indefinite" begin={`${i * 0.6}s`} />
          </circle>
        ))}

        {/* Impact metrics floating */}
        <g opacity="0.9">
          <rect x="140" y="300" width="120" height="36" rx="8" fill="#141c2e" stroke="rgba(0,102,255,0.3)" strokeWidth="1" />
          <text x="200" y="315" textAnchor="middle" fill="#64748b" fontSize="7">Impact Analysis</text>
          <text x="200" y="328" textAnchor="middle" fill="#0066ff" fontSize="10" fontWeight="700">&lt; 5 min</text>
        </g>
      </svg>
    </div>
  );
}
