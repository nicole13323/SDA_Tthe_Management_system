import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { api } from '../api.js';
import { dateOnly, money } from '../format.js';
import PaymentsTable from './PaymentsTable.jsx';
import ReceiptModal from './ReceiptModal.jsx';

/** Weekly / monthly / yearly tithe report. `path` is the report endpoint, `params` extra query params. */
export default function Report({ path, params = {}, showMember = true, showChurch = false }) {
  const { t, i18n } = useTranslation();
  const lng = i18n.resolvedLanguage;
  const [period, setPeriod] = useState('MONTH');
  const [offset, setOffset] = useState(0);
  const [page, setPage] = useState(0);
  const [data, setData] = useState(null);
  const [error, setError] = useState('');
  const [receipt, setReceipt] = useState(null);
  const extra = JSON.stringify(params);

  useEffect(() => {
    let alive = true;
    setData(null);
    setError('');
    api(path, { params: { period, offset, page, size: 15, ...params } })
      .then((d) => alive && setData(d))
      .catch((e) => alive && setError(e.message));
    return () => { alive = false; };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [path, period, offset, page, extra]);

  const choose = (p) => { setPeriod(p); setOffset(0); setPage(0); };
  const move = (delta) => { setOffset((o) => o + delta); setPage(0); };

  return (
    <section className="card">
      <div className="report-head">
        <h2>{t('reportTitle')}</h2>
        <div className="segmented" role="group">
          {['WEEK', 'MONTH', 'YEAR'].map((p) => (
            <button key={p} className={period === p ? 'seg active' : 'seg'} onClick={() => choose(p)}>
              {t(p.toLowerCase())}
            </button>
          ))}
        </div>
      </div>

      <div className="range">
        <button className="btn ghost small" onClick={() => move(-1)}>{t('previous')}</button>
        <strong>{data ? `${dateOnly(data.from, lng)} – ${dateOnly(data.to, lng)}` : '…'}</strong>
        <button className="btn ghost small" onClick={() => move(1)} disabled={offset >= 0}>{t('next')}</button>
      </div>

      {error && <p className="alert error">{t('error')}</p>}
      {!data && !error && <p className="muted">{t('loading')}</p>}
      {data && (
        <>
          <div className="summary">
            <div><span className="muted">{t('totalCollected')}</span><strong className="figure">{money(data.total, lng)}</strong></div>
            <div><span className="muted">{t('paymentCount')}</span><strong className="figure">{data.count}</strong></div>
          </div>
          <PaymentsTable items={data.items} showMember={showMember} showChurch={showChurch} onReceipt={setReceipt} />
          {data.totalPages > 1 && (
            <div className="pager">
              <button className="btn ghost small" disabled={page === 0} onClick={() => setPage(page - 1)}>{t('previous')}</button>
              <span>{page + 1} / {data.totalPages}</span>
              <button className="btn ghost small" disabled={page + 1 >= data.totalPages} onClick={() => setPage(page + 1)}>{t('next')}</button>
            </div>
          )}
        </>
      )}
      {receipt && <ReceiptModal receiptNo={receipt} onClose={() => setReceipt(null)} />}
    </section>
  );
}
