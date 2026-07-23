import { Fragment } from 'react';
import StatusBadge from '../common/StatusBadge';
import './RiskHeatmap.css';

const CELL_LEVELS = ['low', 'medium', 'high', 'critical'];

export default function RiskHeatmap({ data }) {
  const { rows, columns, values, overallScore, overallLevel } = data;

  return (
    <div className="risk-heatmap">
      <div className="risk-heatmap__grid">
        <div className="risk-heatmap__corner" />
        {columns.map((col) => (
          <div key={col} className="risk-heatmap__col-header">
            {col}
          </div>
        ))}
        {rows.map((row, rowIdx) => (
          <Fragment key={row}>
            <div className="risk-heatmap__row-header">{row}</div>
            {values[rowIdx].map((val, colIdx) => (
              <div
                key={`${row}-${colIdx}`}
                className={`risk-heatmap__cell risk-heatmap__cell--${CELL_LEVELS[val]}`}
                title={`${row}: ${columns[colIdx]} risk`}
              />
            ))}
          </Fragment>
        ))}
      </div>
      <div className="risk-heatmap__overall">
        <div className="risk-heatmap__overall-label">Overall Risk Score</div>
        <div className="risk-heatmap__overall-score">{overallScore}/100</div>
        <StatusBadge label={overallLevel} level={overallLevel} size="lg" />
      </div>
    </div>
  );
}
