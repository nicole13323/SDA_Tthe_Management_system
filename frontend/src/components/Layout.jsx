import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth.jsx';

const LANGUAGES = [['rw', 'Kinyarwanda'], ['sw', 'Kiswahili'], ['en', 'English'], ['fr', 'Français']];

export function LanguageSwitcher() {
  const { i18n, t } = useTranslation();
  return (
    <label className="lang">
      <span className="sr-only">{t('language')}</span>
      <select value={i18n.resolvedLanguage} onChange={(e) => i18n.changeLanguage(e.target.value)}>
        {LANGUAGES.map(([code, name]) => <option key={code} value={code}>{name}</option>)}
      </select>
    </label>
  );
}

export default function Layout({ children }) {
  const { t } = useTranslation();
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  return (
    <>
      <div className="flagband" aria-hidden="true" />
      <header className="topbar">
        <div className="brand">{t('appName')}</div>
        <div className="topbar-right">
          <LanguageSwitcher />
          {user && (
            <>
              <span className="who">{user.name}</span>
              <button className="btn ghost" onClick={() => { logout(); navigate('/login'); }}>{t('logout')}</button>
            </>
          )}
        </div>
      </header>
      <main className="container">{children}</main>
    </>
  );
}
