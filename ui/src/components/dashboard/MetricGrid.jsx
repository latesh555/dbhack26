import {
  AppWindow,
  Server,
  Globe,
  Database,
  Users,
  GitBranch,
  Link2,
} from 'lucide-react';
import './MetricGrid.css';

const ICON_MAP = {
  applications: AppWindow,
  microservices: Server,
  apis: Globe,
  databases: Database,
  businessTeams: Users,
  repositories: GitBranch,
  dependencies: Link2,
};

const LABEL_MAP = {
  applications: 'Affected Applications',
  microservices: 'Microservices',
  apis: 'APIs',
  databases: 'Databases',
  businessTeams: 'Business Teams',
  repositories: 'Repositories',
  dependencies: 'Dependencies',
};

export default function MetricGrid({ metrics }) {
  return (
    <div className="metric-grid">
      {Object.entries(metrics).map(([key, value]) => {
        const Icon = ICON_MAP[key];
        return (
          <div key={key} className="metric-grid__item">
            {Icon && (
              <div className="metric-grid__icon">
                <Icon size={16} />
              </div>
            )}
            <span className="metric-grid__value">{value}</span>
            <span className="metric-grid__label">{LABEL_MAP[key]}</span>
          </div>
        );
      })}
    </div>
  );
}
