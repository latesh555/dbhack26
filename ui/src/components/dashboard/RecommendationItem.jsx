import SparkleIcon from '../common/SparkleIcon';
import './RecommendationItem.css';

export default function RecommendationItem({ priority, text, confidence }) {
  return (
    <div className="recommendation-item">
      <div className="recommendation-item__priority">
        <SparkleIcon size={12} />
        <span>Priority {priority}</span>
      </div>
      <p className="recommendation-item__text">{text}</p>
      <div className="recommendation-item__confidence">
        <div className="recommendation-item__confidence-bar">
          <div
            className="recommendation-item__confidence-fill"
            style={{ width: `${confidence}%` }}
          />
        </div>
        <span>{confidence}% confidence</span>
      </div>
    </div>
  );
}
