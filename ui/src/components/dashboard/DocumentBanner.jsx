import { FileText, Calendar, Clock, AlertTriangle, Sparkles } from 'lucide-react';
import StatusBadge from '../common/StatusBadge';
import './DocumentBanner.css';

export default function DocumentBanner({ document }) {
  const meta = [
    { icon: FileText, label: 'Document Type', value: document.type },
    { icon: Calendar, label: 'Published', value: document.published },
    { icon: Clock, label: 'Effective', value: document.effective },
    { icon: AlertTriangle, label: 'Severity', value: document.severity, badge: true },
    { icon: Sparkles, label: 'Confidence', value: `${document.confidence}%` },
  ];

  return (
    <div className="doc-banner">
      <div className="doc-banner__meta">
        {meta.map(({ icon: Icon, label, value, badge }) => (
          <div key={label} className="doc-banner__meta-item">
            <Icon size={14} className="doc-banner__meta-icon" />
            <div>
              <span className="doc-banner__meta-label">{label}</span>
              {badge ? (
                <StatusBadge label={value} level={value} size="sm" />
              ) : (
                <span className="doc-banner__meta-value">{value}</span>
              )}
            </div>
          </div>
        ))}
      </div>
      <div className="doc-banner__summary">
        <h3 className="doc-banner__summary-title">
          <Sparkles size={16} />
          AI Summary
        </h3>
        <p className="doc-banner__summary-text">&ldquo;{document.summary}&rdquo;</p>
      </div>
    </div>
  );
}
