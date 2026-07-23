import { AlertTriangle, RotateCcw, ListOrdered, CheckSquare, Sparkles } from 'lucide-react';
import './EngineeringPanel.css';

function PanelSection({ icon: Icon, title, items, ordered = false }) {
  const Tag = ordered ? 'ol' : 'ul';
  return (
    <div className="eng-panel__section">
      <div className="eng-panel__section-header">
        <Icon size={16} />
        <h4>{title}</h4>
      </div>
      <Tag className="eng-panel__list">
        {items.map((item, i) => (
          <li key={i}>{item}</li>
        ))}
      </Tag>
    </div>
  );
}

export default function EngineeringPanel({ recommendations }) {
  return (
    <div className="eng-panel">
      <div className="eng-panel__header">
        <Sparkles size={18} />
        <h3>AI Engineering Recommendations</h3>
      </div>
      <PanelSection
        icon={AlertTriangle}
        title="Potential Risks"
        items={recommendations.potentialRisks}
      />
      <PanelSection
        icon={RotateCcw}
        title="Rollback Strategy"
        items={recommendations.rollbackStrategy}
      />
      <PanelSection
        icon={ListOrdered}
        title="Deployment Order"
        items={recommendations.deploymentOrder}
        ordered
      />
      <PanelSection
        icon={CheckSquare}
        title="Testing Checklist"
        items={recommendations.testingChecklist}
      />
    </div>
  );
}
