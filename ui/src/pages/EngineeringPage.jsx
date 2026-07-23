import { useNavigate } from 'react-router-dom';
import {
  ArrowLeft,
  Code2,
  Users,
  Calendar,
  Kanban,
  Download,
  FileText,
  CheckSquare,
  ClipboardList,
  FileBarChart,
} from 'lucide-react';
import Header from '../components/layout/Header';
import Button from '../components/common/Button';
import Card from '../components/common/Card';
import KPICard from '../components/common/KPICard';
import Timeline from '../components/engineering/Timeline';
import StoryCard from '../components/engineering/StoryCard';
import EngineeringPanel from '../components/engineering/EngineeringPanel';
import {
  engineeringSummary,
  engineeringTeams,
  timelinePhases,
  jiraStories,
  engineeringRecommendations,
} from '../data/mockData';
import './EngineeringPage.css';

export default function EngineeringPage() {
  const navigate = useNavigate();

  return (
    <div className="engineering-page">
      <Header />

      <div className="engineering-page__content">
        <button className="engineering-page__back" onClick={() => navigate('/dashboard')}>
          <ArrowLeft size={16} />
          Back to Dashboard
        </button>

        <div className="engineering-page__header">
          <h1 className="engineering-page__title">Engineering Impact Analysis</h1>
          <div className="engineering-page__summary">
            <KPICard label="Overall Effort" value={`${engineeringSummary.storyPoints} Story Points`} icon={Code2} variant="accent" />
            <KPICard label="Teams" value={engineeringSummary.teams} icon={Users} />
            <KPICard label="Estimated Completion" value={`${engineeringSummary.estimatedDays} Days`} icon={Calendar} />
          </div>
        </div>

        <div className="engineering-page__layout">
          <div className="engineering-page__main">
            {/* Teams */}
            <Card title="Teams Involved" className="engineering-page__teams-card">
              <div className="engineering-page__teams">
                {engineeringTeams.map((team) => (
                  <div key={team} className="engineering-page__team">
                    <Users size={14} />
                    <span>{team}</span>
                  </div>
                ))}
              </div>
            </Card>

            {/* Timeline */}
            <Card title="Timeline" className="engineering-page__timeline-card">
              <Timeline phases={timelinePhases} />
            </Card>

            {/* Jira Stories */}
            <div className="engineering-page__stories">
              <h2 className="engineering-page__stories-title">AI Generated Jira Stories</h2>
              <StoryCard story="Sanctions Update July 2026" isEpic />
              {jiraStories.map((story, index) => (
                <StoryCard key={story.id} story={story} index={index + 1} />
              ))}
            </div>

            {/* Bottom Actions */}
            <div className="engineering-page__actions">
              <Button variant="primary" icon={Kanban}>Generate Jira Tickets</Button>
              <Button variant="secondary" icon={Download}>Download Engineering Plan</Button>
              <Button variant="secondary" icon={CheckSquare}>Generate Test Cases</Button>
              <Button variant="secondary" icon={ClipboardList}>Create Deployment Checklist</Button>
              <Button variant="secondary" icon={FileBarChart}>Generate Executive Summary</Button>
            </div>
          </div>

          <EngineeringPanel recommendations={engineeringRecommendations} />
        </div>
      </div>
    </div>
  );
}
