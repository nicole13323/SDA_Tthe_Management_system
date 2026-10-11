import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { api } from '../api.js';
import { useAuth } from '../auth.jsx';
import ReceiptModal from '../components/ReceiptModal.jsx';
import Report from '../components/Report.jsx';
import Tabs from '../components/Tabs.jsx';
import { money } from '../format.js';

function AllChurchesReport() {
  const { t } = useTranslation();
  const [churches, setChurches] = useState([]);
  const [churchId, setChurchId] = useState('');
  useEffect(() => { api('/manager/churches').then(setChurches).catch(() => {}); }, []);
  return (
    <>
      <label className="filter">
        {t('church')}
        <select value={churchId} onChange={(e) => setChurchId(e.target.value)}>
          <option value="">{t('allChurches')}</option>
          {churches.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
        </select>
      </label>
      <Report path="/manager/payments" params={{ churchId }} showMember showChurch />
    </>
  );
}

function CashPayment() {
  const { t, i18n } = useTranslation();
  const [q, setQ] = useState('');
  const [results, setResults] = useState([]);
  const [member, setMember] = useState(null);
  const [amount, setAmount] = useState('');
  const [msg, setMsg] = useState(null);
  const [busy, setBusy] = useState(false);
  const [receipt, setReceipt] = useState(null);

  useEffect(() => {
    if (q.trim().length < 2) { setResults([]); return undefined; }
    const timer = setTimeout(() => api('/manager/members', { params: { q } }).then(setResults).catch(() => {}), 300);
    return () => clearTimeout(timer);
  }, [q]);

  async function submit(e) {
    e.preventDefault();
    if (!member) return;
    setBusy(true);
    setMsg(null);
    try {
      const p = await api('/manager/payments/cash', { method: 'POST', body: { memberId: member.id, amount: Number(amount) } });
      setMsg({ type: 'ok', text: `${t('cashRecorded')}: ${money(p.amount, i18n.resolvedLanguage)}` });
      setReceipt(p.receiptNo);
      setAmount('');
      setMember(null);
      setQ('');
    } catch (err) {
      setMsg({ type: 'error', text: err.message === 'error' ? t('error') : err.message });
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className="grid-2">
      <section className="card">
        <h2>{t('searchMember')}</h2>
        <input type="search" placeholder={t('searchPlaceholder')} value={q} onChange={(e) => setQ(e.target.value)} />
        <ul className="pick-list">
          {results.map((m) => (
            <li key={m.id}>
              <div><strong>{m.fullName}</strong><br /><span className="muted small">{m.phone} · {m.churchName}</span></div>
              <button type="button" className="btn ghost small" onClick={() => setMember(m)}>{t('selectMember')}</button>
            </li>
          ))}
        </ul>
      </section>

      <form className="card" onSubmit={submit}>
        <h2>{t('cashTitle')}</h2>
        {member
          ? <p className="selected"><strong>{member.fullName}</strong><br /><span className="muted small">{member.phone} · {member.churchName}</span></p>
          : <p className="muted">{t('searchMember')}</p>}
        <label>{t('amount')} (RWF)
          <input type="number" min="100" step="100" inputMode="numeric" value={amount}
                 onChange={(e) => setAmount(e.target.value)} required />
        </label>
        {msg && <p className={`alert ${msg.type}`}>{msg.text}</p>}
        <button className="btn" disabled={busy || !member}>{t('recordCash')}</button>
      </form>
      {receipt && <ReceiptModal receiptNo={receipt} onClose={() => setReceipt(null)} />}
    </div>
  );
}

export default function ManagerDashboard() {
  const { t } = useTranslation();
  const { user } = useAuth();
  const [tab, setTab] = useState('report');
  return (
    <>
      <h1>{t('hello', { name: user.name })}</h1>
      <Tabs value={tab} onChange={setTab} tabs={[['report', t('reportTitle')], ['cash', t('cashTitle')]]} />
      {tab === 'report' ? <AllChurchesReport /> : <CashPayment />}
    </>
  );
}
