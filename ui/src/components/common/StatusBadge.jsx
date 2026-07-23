import './StatusBadge.css';

const LEVEL_MAP = {
  low: 'low',
  medium: 'medium',
  high: 'high',
  critical: 'critical',
  'Low Risk': 'low',
  'Medium Risk': 'medium',
  'High Risk': 'high',
  Critical: 'critical',
  High: 'high',
  Immediate: 'critical',
};

export default function StatusBadge({ label, level, size = 'md' }) {
  const resolvedLevel = LEVEL_MAP[level] || LEVEL_MAP[label] || 'medium';

  return (
    <span className={`status-badge status-badge--${resolvedLevel} status-badge--${size}`}>
      {label}
    </span>
  );
}
