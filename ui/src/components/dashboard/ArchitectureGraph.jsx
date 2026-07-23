import { architectureNodes, architectureEdges } from '../../data/mockData';
import './ArchitectureGraph.css';

const NODE_COLORS = {
  regulation: '#8b5cf6',
  process: '#3b82f6',
  service: '#06b6d4',
  api: '#10b981',
  database: '#f59e0b',
  owner: '#64748b',
};

export default function ArchitectureGraph() {
  const nodeMap = Object.fromEntries(architectureNodes.map((n) => [n.id, n]));

  return (
    <div className="arch-graph">
      <svg viewBox="0 0 100 100" className="arch-graph__svg">
        <defs>
          <linearGradient id="edge-gradient" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="0%" stopColor="#0066ff" stopOpacity="0.4" />
            <stop offset="100%" stopColor="#8b5cf6" stopOpacity="0.4" />
          </linearGradient>
        </defs>
        {architectureEdges.map(([from, to], i) => {
          const source = nodeMap[from];
          const target = nodeMap[to];
          if (!source || !target) return null;
          return (
            <line
              key={i}
              x1={source.x}
              y1={source.y}
              x2={target.x}
              y2={target.y}
              stroke="url(#edge-gradient)"
              strokeWidth="0.3"
              strokeDasharray="1 0.5"
            />
          );
        })}
        {architectureNodes.map((node) => (
          <g key={node.id} className="arch-graph__node">
            <circle
              cx={node.x}
              cy={node.y}
              r={node.type === 'regulation' ? 3.5 : 2.5}
              fill={NODE_COLORS[node.type]}
              opacity="0.9"
            />
            <text
              x={node.x}
              y={node.y + (node.type === 'regulation' ? 5.5 : 4.5)}
              textAnchor="middle"
              className="arch-graph__label"
            >
              {node.label}
            </text>
          </g>
        ))}
      </svg>
      <div className="arch-graph__legend">
        {Object.entries(NODE_COLORS).map(([type, color]) => (
          <span key={type} className="arch-graph__legend-item">
            <span className="arch-graph__legend-dot" style={{ background: color }} />
            {type.charAt(0).toUpperCase() + type.slice(1)}
          </span>
        ))}
      </div>
    </div>
  );
}
