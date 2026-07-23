import './RiskMeter.css';

export default function RiskMeter({ score, maxScore = 100, label }) {
  const percentage = (score / maxScore) * 100;
  const level = score >= 80 ? 'critical' : score >= 60 ? 'high' : score >= 40 ? 'medium' : 'low';

  return (
    <div className="risk-meter">
      <div className="risk-meter__header">
        <span className="risk-meter__label">{label || 'Business Impact Score'}</span>
        <span className={`risk-meter__score risk-meter__score--${level}`}>
          {score}/{maxScore}
        </span>
      </div>
      <div className="risk-meter__track">
        <div
          className={`risk-meter__fill risk-meter__fill--${level}`}
          style={{ width: `${percentage}%` }}
        />
      </div>
      <div className="risk-meter__scale">
        <span>Low</span>
        <span>Medium</span>
        <span>High</span>
        <span>Critical</span>
      </div>
    </div>
  );
}
