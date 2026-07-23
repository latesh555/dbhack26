import {
  Kanban,
  FileBarChart,
  Mail,
  Presentation,
  Download,
} from 'lucide-react';
import SparkleIcon from '../common/SparkleIcon';
import './ActionCard.css';

const ICON_MAP = {
  jira: Kanban,
  report: FileBarChart,
  email: Mail,
  ppt: Presentation,
  export: Download,
};

export default function ActionCard({ title, icon, description, onClick }) {
  const Icon = ICON_MAP[icon] || Kanban;

  return (
    <button className="action-card" onClick={onClick}>
      <div className="action-card__icon">
        <Icon size={20} />
      </div>
      <div className="action-card__content">
        <span className="action-card__title">{title}</span>
        {description && <span className="action-card__desc">{description}</span>}
      </div>
      <SparkleIcon size={14} className="action-card__sparkle" />
    </button>
  );
}
