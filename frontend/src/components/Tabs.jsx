export default function Tabs({ tabs, value, onChange }) {
  return (
    <div className="tabs" role="tablist">
      {tabs.map(([key, label]) => (
        <button key={key} role="tab" aria-selected={value === key}
                className={value === key ? 'tab active' : 'tab'} onClick={() => onChange(key)}>
          {label}
        </button>
      ))}
    </div>
  );
}
