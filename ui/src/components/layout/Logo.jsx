import { Shield } from 'lucide-react';
import './Logo.css';

export default function Logo({ size = 'md', showTagline = false }) {
  return (
    <div className={`logo logo--${size}`}>
      <div className="logo__icon">
        <Shield size={size === 'lg' ? 28 : size === 'sm' ? 18 : 22} />
      </div>
      <div className="logo__text">
        <span className="logo__name">
          RegIntel <span className="logo__ai">AI</span>
        </span>
        {showTagline && (
          <span className="logo__tagline">
            From regulatory update to business impact in under 5 minutes.
          </span>
        )}
      </div>
    </div>
  );
}
