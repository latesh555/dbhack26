import StatusBadge from '../common/StatusBadge';
import {
  User,
  GitBranch,
  Clock,
  CheckSquare,
  Globe,
  Server,
} from 'lucide-react';
import './StoryCard.css';

export default function StoryCard({ story, index, isEpic = false }) {
  if (isEpic) {
    return (
      <div className="story-card story-card--epic">
        <span className="story-card__epic-label">Epic</span>
        <h3 className="story-card__epic-title">{story}</h3>
      </div>
    );
  }

  const fields = [
    { icon: User, label: 'Owner', value: story.owner },
    { icon: GitBranch, label: 'Dependencies', value: story.dependencies.join(', ') },
    { icon: Clock, label: 'Duration', value: story.estimatedDuration },
    { icon: Globe, label: 'Affected APIs', value: story.affectedApis.join(', ') },
    { icon: Server, label: 'Microservices', value: story.affectedMicroservices.join(', ') },
  ];

  return (
    <div className="story-card">
      <div className="story-card__header">
        <div className="story-card__number">Story {index}</div>
        <h4 className="story-card__title">{story.title}</h4>
        <div className="story-card__badges">
          <StatusBadge label={story.priority} level={story.priority} size="sm" />
          <span className="story-card__points">{story.storyPoints} SP</span>
        </div>
      </div>
      <div className="story-card__fields">
        {fields.map(({ icon: Icon, label, value }) => (
          <div key={label} className="story-card__field">
            <Icon size={14} />
            <div>
              <span className="story-card__field-label">{label}</span>
              <span className="story-card__field-value">{value}</span>
            </div>
          </div>
        ))}
      </div>
      <div className="story-card__criteria">
        <div className="story-card__criteria-header">
          <CheckSquare size={14} />
          <span>Acceptance Criteria</span>
        </div>
        <ul>
          {story.acceptanceCriteria.map((criteria, i) => (
            <li key={i}>{criteria}</li>
          ))}
        </ul>
      </div>
    </div>
  );
}
