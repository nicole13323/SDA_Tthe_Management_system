import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { api } from '../api.js';
import { useAuth } from '../auth.jsx';
import PaymentsTable from '../components/PaymentsTable.jsx';
import ReceiptModal from '../components/ReceiptModal.jsx';

const METHODS = ['MTN_MOMO', 'AIRTEL_MONEY', 'CARD', 'BANK']; // cash is never offered to members

export default function MemberDashboard() {
  const { t } = useTranslation();
  const { user } = useAuth();
  const [history, setHistory] = useState(null);
  const [form, setForm] = useState({ amount: '', method: 'MTN_MOMO', payerPhone: '' });
  const [busy, setBusy] = useState(false);
  const [msg, setMsg] = useState(null);
  const [receipt, setReceipt] = useState(null);
  const isMomo = form.method === 'MTN_MOMO' || form.method === 'AIRTEL_MONEY';

  const load = () => api('/payments/me').then(setHistory).catch(() => setMsg({ type: 'error', text: t('error') }));
  useEffect(() => { load(); }, []); // eslint-disable-line react-hooks/exhaustive-deps

  async function submit(e) {
    e.preventDefault();
    setBusy(true);
    setMsg(null);
    try {
      const p = await api('/payments', {
        method: 'POST',
        body: { amount: Number(form.amount), method: form.method, payerPhone: isMomo && form.payerPhone ? form.payerPhone : null }
      });
      setMsg({ type: 'ok', text: t('paySuccess') });
      setForm((f) => ({ ...f, amount: '' }));
      setReceipt(p.receiptNo);
      load();
    } catch (err) {
      setMsg({ type: 'error', text: err.message === 'error' ? t('error') : err.message });
    } finally {
      setBusy(false);
    }
  }

  return (
    <>
      <h1>{t('hello', { name: user.name })}</h1>
      <div className="grid-2">
        <form className="card" onSubmit={submit}>
          <h2>{t('payTitle')}</h2>
          <label>
            {t('amount')} (RWF)
            <input type="number" min="100" step="100" inputMode="numeric" value={form.amount}
                   onChange={(e) => setForm({ ...form, amount: e.target.value })} required />
          </label>
          <label>
            {t('method')}
            <select value={form.method} onChange={(e) => setForm({ ...form, method: e.target.value })}>
              {METHODS.map((m) => <option key={m} value={m}>{t(`methods.${m}`)}</option>)}
            </select>
          </label>
          {isMomo && (
            <label>
              {t('payerPhone')}
              <input type="tel" inputMode="tel" placeholder="07XXXXXXXX" value={form.payerPhone}
                     onChange={(e) => setForm({ ...form, payerPhone: e.target.value })} />
            </label>
          )}
          {msg && <p className={`alert ${msg.type}`}>{msg.text}</p>}
          <button className="btn" disabled={busy}>{busy ? t('paying') : t('payNow')}</button>
        </form>

        <section className="card">
          <h2>{t('history')}</h2>
          {history === null ? <p className="muted">{t('loading')}</p>
            : history.length === 0 ? <p className="muted">{t('noHistory')}</p>
            : <PaymentsTable items={history} onReceipt={setReceipt} />}
        </section>
      </div>
      {receipt && <ReceiptModal receiptNo={receipt} onClose={() => setReceipt(null)} />}
    </>
  );
}
