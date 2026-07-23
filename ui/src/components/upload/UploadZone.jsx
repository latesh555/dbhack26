import { Upload, FileText, FileSpreadsheet, FileCode, FileJson, Link2 } from 'lucide-react';
import './UploadZone.css';

const FORMATS = [
  { ext: 'PDF', icon: FileText, color: '#ef4444' },
  { ext: 'DOCX', icon: FileText, color: '#3b82f6' },
  { ext: 'CSV', icon: FileSpreadsheet, color: '#10b981' },
  { ext: 'XML', icon: FileCode, color: '#f59e0b' },
  { ext: 'JSON', icon: FileJson, color: '#8b5cf6' },
];

export default function UploadZone({ onFileSelect }) {
  const handleDrop = (e) => {
    e.preventDefault();
    if (e.dataTransfer.files.length > 0) {
      onFileSelect?.(e.dataTransfer.files[0]);
    }
  };

  const handleDragOver = (e) => {
    e.preventDefault();
  };

  return (
    <div className="upload-zone-wrapper">
      <div
        className="upload-zone"
        onDrop={handleDrop}
        onDragOver={handleDragOver}
        onClick={() => document.getElementById('file-input')?.click()}
      >
        <input
          id="file-input"
          type="file"
          accept=".pdf,.docx,.csv,.xml,.json"
          hidden
          onChange={(e) => e.target.files[0] && onFileSelect?.(e.target.files[0])}
        />
        <div className="upload-zone__icon">
          <Upload size={32} />
        </div>
        <p className="upload-zone__title">Drag & drop your document here</p>
        <p className="upload-zone__subtitle">or click to browse files</p>
        <div className="upload-zone__formats">
          {FORMATS.map(({ ext, icon: Icon, color }) => (
            <div key={ext} className="upload-zone__format" style={{ '--format-color': color }}>
              <Icon size={16} />
              <span>{ext}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="upload-zone__divider">
        <span>OR</span>
      </div>

      <div className="upload-zone__url">
        <Link2 size={16} />
        <input type="url" placeholder="Paste Document URL" />
      </div>
    </div>
  );
}
