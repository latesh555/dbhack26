import { Search } from 'lucide-react';
import './SearchBar.css';

export default function SearchBar({ placeholder = 'Search documents, regulations, impacts...' }) {
  return (
    <div className="search-bar">
      <Search size={16} className="search-bar__icon" />
      <input
        type="text"
        className="search-bar__input"
        placeholder={placeholder}
      />
      <kbd className="search-bar__shortcut">⌘K</kbd>
    </div>
  );
}
