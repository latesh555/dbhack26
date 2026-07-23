import { useNavigate } from 'react-router-dom';
import { Sparkles, FileText, ArrowRight } from 'lucide-react';
import Logo from '../components/layout/Logo';
import UploadZone from '../components/upload/UploadZone';
import HeroIllustration from '../components/upload/HeroIllustration';
import Button from '../components/common/Button';
import { recentUploads } from '../data/mockData';
import './UploadPage.css';

export default function UploadPage() {
  const navigate = useNavigate();

  const handleAnalyze = () => {
    navigate('/dashboard');
  };

  return (
    <div className="upload-page">
      <div className="upload-page__bg" />

      <header className="upload-page__header">
        <Logo size="md" />
      </header>

      <main className="upload-page__main">
        <div className="upload-page__content">
          <div className="upload-page__hero">
            <h1 className="upload-page__title">
              RegIntel <span className="upload-page__title-accent">AI</span>
            </h1>
            <p className="upload-page__tagline">
              From regulatory update to business impact in under 5 minutes.
            </p>
            <p className="upload-page__subtitle">
              Upload a regulatory document, sanctions update, policy, advisory, or compliance
              notice and receive an AI-powered impact assessment across the enterprise.
            </p>

            <UploadZone />

            <Button
              variant="primary"
              size="lg"
              icon={Sparkles}
              onClick={handleAnalyze}
              className="upload-page__cta"
            >
              Analyze Document
            </Button>

            <div className="upload-page__recent">
              <span className="upload-page__recent-label">Recent uploads</span>
              <div className="upload-page__recent-list">
                {recentUploads.map((item) => (
                  <button
                    key={item.id}
                    className="upload-page__recent-item"
                    onClick={handleAnalyze}
                  >
                    <FileText size={14} />
                    <span>{item.name}</span>
                    <ArrowRight size={14} className="upload-page__recent-arrow" />
                  </button>
                ))}
              </div>
            </div>
          </div>

          <div className="upload-page__illustration">
            <HeroIllustration />
          </div>
        </div>
      </main>
    </div>
  );
}
