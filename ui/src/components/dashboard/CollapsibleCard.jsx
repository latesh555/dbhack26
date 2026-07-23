import { useState } from 'react';
import { ChevronDown, ChevronRight, Code2, Users, Calendar } from 'lucide-react';
import Card from '../common/Card';
import Button from '../common/Button';
import './CollapsibleCard.css';

export default function CollapsibleCard({
  title,
  summary,
  children,
  defaultExpanded = false,
  onNavigate,
}) {
  const [expanded, setExpanded] = useState(defaultExpanded);

  return (
    <Card className="collapsible-card">
      <div
        className="collapsible-card__header"
        onClick={() => setExpanded(!expanded)}
        role="button"
        tabIndex={0}
        onKeyDown={(e) => e.key === 'Enter' && setExpanded(!expanded)}
      >
        <div className="collapsible-card__title-row">
          {expanded ? <ChevronDown size={18} /> : <ChevronRight size={18} />}
          <h3 className="collapsible-card__title">{title}</h3>
        </div>
        {!expanded && summary && (
          <div className="collapsible-card__summary">
            {summary.map(({ icon: Icon, label, value }) => (
              <div key={label} className="collapsible-card__stat">
                <Icon size={16} />
                <div>
                  <span className="collapsible-card__stat-value">{value}</span>
                  <span className="collapsible-card__stat-label">{label}</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
      {expanded && children && (
        <div className="collapsible-card__body">{children}</div>
      )}
      {!expanded && onNavigate && (
        <div className="collapsible-card__footer">
          <Button variant="ghost" onClick={onNavigate}>
            View Engineering Details →
          </Button>
        </div>
      )}
    </Card>
  );
}

export { Code2, Users, Calendar };
