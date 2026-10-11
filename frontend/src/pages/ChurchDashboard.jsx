import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { api } from '../api.js';
import { useAuth } from '../auth.jsx';
import Report from '../components/Report.jsx';
import Tabs from '../components/Tabs.jsx';

const LANGS = [['rw', 'Kinyarwanda'], ['sw', 'Kiswahili'], ['en', 'English'], ['fr', 'Français']];
const EMPTY = { fullName: '', phone: '', email: '', preferredLanguage: 'rw' };

function Members() {
  const { t } = useTranslation();
  const [members, setMembers] = useState(null);
  const [form, setForm] = useState(EMPTY);
  const [msg, setMsg] = useState(null);
  const [busy, setBusy] = useState(false);

  const load = () => api('/church/members').then(setMembers).catch(() => setMsg({ type: 'error', text: t('error') }));
  useEffect(() => { load(); }, []); // eslint-disable-line react-hooks/exhaustive-deps

  async function submit(e) {
    e.preventDefault();
    setBusy(true);
    setMsg(null);
    try {
      await api('/church/members', {
        method: 'POST',
        body: { ...form, email: form.email.trim() || null }
      });
      setMsg({ type: 'ok', text: t('memberAdded') });
      setForm(EMPTY);
      load();
    } catch (err) {
      setMsg({ type: 'error', text: err.message === 'error' ? t('error') : err.message });
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className="grid-2">
      <form className="card" onSubmit={submit}>
        <h2>{t('addMemberTitle')}</h2>
        <label>{t('fullName')}
          <input value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} required />
        </label>
        <label>{t('phone')}
          <input type="tel" placeholder="07XXXXXXXX" value={form.phone}
                 onChange={(e) => setForm({ ...form, phone: e.target.value })} required />
        </label>
        <label>{t('email')}
          <input type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
        </label>
        <label>{t('language')}
          <select value={form.preferredLanguage} onChange={(e) => setForm({ ...form, preferredLanguage: e.target.value })}>
            {LANGS.map(([c, n]) => <option key={c} value={c}>{n}</option>)}
          </select>
        </label>
        {msg && <p className={`alert ${msg.type}`}>{msg.text}</p>}
        <button className="btn" disabled={busy}>{t('save')}</button>
      </form>

      <section className="card">
        <h2>{t('members')}{members ? ` (${members.length})` : ''}</h2>
        {members === null ? <p className="muted">{t('loading')}</p>
          : members.length === 0 ? <p className="muted">{t('noMembers')}</p>
          : (
            <div className="table-wrap">
              <table>
                <thead><tr><th>{t('fullName')}</th><th>{t('phone')}</th><th>{t('email')}</th></tr></thead>
                <tbody>
                  {members.map((m) => (
                    <tr key={m.id}><td>{m.fullName}</td><td>{m.phone}</td><td>{m.email || '—'}</td></tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
      </section>
    </div>
  );
}

export default function ChurchDashboard() {
  const { t } = useTranslation();
  const { user } = useAuth();
  const [tab, setTab] = useState('report');
  return (
    <>
      <h1>{user.name}</h1>
      <Tabs value={tab} onChange={setTab} tabs={[['report', t('reportTitle')], ['members', t('members')]]} />
      {tab === 'report' ? <Report path="/church/payments" showMember /> : <Members />}
    </>
  );
}
