import { Sparkles } from 'lucide-react';
import RecommendationItem from './RecommendationItem';
import './Sidebar.css';

export default function Sidebar({ recommendations, title = 'AI Recommendations' }) {
  return (
    <aside className="sidebar">
      <div className="sidebar__header">
        <Sparkles size={18} className="sidebar__header-icon" />
        <h3>{title}</h3>
      </div>
      <div className="sidebar__list">
        {recommendations.map((rec) => (
          <RecommendationItem key={rec.priority} {...rec} />
        ))}
      </div>
    </aside>
  );
}
