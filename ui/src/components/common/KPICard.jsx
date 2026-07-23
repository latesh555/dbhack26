import { TrendingUp, TrendingDown, Minus } from 'lucide-react';
import './KPICard.css';

export default function KPICard({
  label,
  value,
  icon: Icon,
  trend,
  trendValue,
  variant = 'default',
  size = 'default',
}) {
  const TrendIcon = trend === 'up' ? TrendingUp : trend === 'down' ? TrendingDown : Minus;

  return (
    <div className={`kpi-card kpi-card--${variant} kpi-card--${size}`}>
      <div className="kpi-card__header">
        {Icon && (
          <div className="kpi-card__icon">
            <Icon size={18} />
          </div>
        )}
        <span className="kpi-card__label">{label}</span>
      </div>
      <div className="kpi-card__value">{value}</div>
      {trend && (
        <div className={`kpi-card__trend kpi-card__trend--${trend}`}>
          <TrendIcon size={14} />
          <span>{trendValue}</span>
        </div>
      )}
    </div>
  );
}
