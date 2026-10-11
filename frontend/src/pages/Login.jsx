import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { homeFor, useAuth } from '../auth.jsx';

export default function Login() {
  const { t } = useTranslation();
  const { login } = useAuth();
  const navigate = useNavigate();
  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('');
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState('');

  async function submit(e) {
    e.preventDefault();
    setBusy(true);
    setError('');
    try {
      const res = await login(identifier, password);
      navigate(homeFor(res.role), { replace: true });
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className="login-bg">
      <div className="login-card">
        <img src="/logo.png" alt="" className="login-logo" />
        <h1>{t('appName')}</h1>
        <p className="muted">{t('loginSubtitle')}</p>
        <form onSubmit={submit}>
          <label>
            {t('identifier')}
            <input value={identifier} onChange={(e) => setIdentifier(e.target.value)} autoComplete="username" required />
          </label>
          <label>
            {t('password')}
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} autoComplete="current-password" required />
          </label>
          {error && <p className="alert error">{error === 'error' ? t('error') : error}</p>}
          <button className="btn wide" disabled={busy}>{busy ? t('signingIn') : t('signIn')}</button>
        </form>
        <p className="muted small">{t('loginHelp')}</p>
      </div>
    </div>
  );
}
