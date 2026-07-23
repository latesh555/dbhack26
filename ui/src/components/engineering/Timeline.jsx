import './Timeline.css';

export default function Timeline({ phases }) {
  return (
    <div className="timeline">
      {phases.map((phase, index) => (
        <div key={phase.phase} className="timeline__item">
          <div className={`timeline__dot timeline__dot--${phase.status}`} />
          {index < phases.length - 1 && (
            <div className={`timeline__line timeline__line--${phase.status}`} />
          )}
          <div className="timeline__content">
            <span className={`timeline__phase timeline__phase--${phase.status}`}>
              {phase.phase}
            </span>
          </div>
        </div>
      ))}
    </div>
  );
}
