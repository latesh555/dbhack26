import { Bell, Settings, User } from 'lucide-react';
import Logo from './Logo';
import SearchBar from '../common/SearchBar';
import './Header.css';

export default function Header({ showSearch = true }) {
  return (
    <header className="header">
      <div className="header__inner">
        <div className="header__left">
          <Logo size="sm" />
        </div>
        {showSearch && (
          <div className="header__center">
            <SearchBar />
          </div>
        )}
        <div className="header__right">
          <button className="header__icon-btn" aria-label="Notifications">
            <Bell size={18} />
            <span className="header__notification-dot" />
          </button>
          <button className="header__icon-btn" aria-label="Settings">
            <Settings size={18} />
          </button>
          <button className="header__profile" aria-label="Profile">
            <User size={16} />
            <span className="header__profile-name">J. Mitchell</span>
          </button>
        </div>
      </div>
    </header>
  );
}
