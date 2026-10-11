import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { api } from '../api.js';
import { dateTime, money } from '../format.js';

export default function ReceiptModal({ receiptNo, onClose }) {
  const { t, i18n } = useTranslation();
  const lng = i18n.resolvedLanguage;
  const [r, setR] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    api(`/receipts/${receiptNo}`).then(setR).catch((e) => setError(e.message));
  }, [receiptNo]);

  return (
    <div className="overlay" role="dialog" aria-modal="true" aria-label={t('receiptTitle')} onClick={onClose}>
      <div className="receipt" onClick={(e) => e.stopPropagation()}>
        <h2>{t('receiptTitle')}</h2>
        {error && <p className="alert error">{t('error')}</p>}
        {!r && !error && <p className="muted">{t('loading')}</p>}
        {r && (
          <dl className="receipt-grid">
            <dt>{t('receipt')}</dt><dd>{r.receiptNo}</dd>
            <dt>{t('reference')}</dt><dd>{r.reference}</dd>
            <dt>{t('member')}</dt><dd>{r.memberName}</dd>
            <dt>{t('church')}</dt><dd>{r.churchName}</dd>
            <dt>{t('method')}</dt><dd>{t(`methods.${r.method}`)}</dd>
            <dt>{t('date')}</dt><dd>{dateTime(r.paidAt, lng)}</dd>
            <dt>{t('amount')}</dt><dd className="big">{money(r.amount, lng)}</dd>
          </dl>
        )}
        <div className="actions no-print">
          {r && <button className="btn" onClick={() => window.print()}>{t('print')}</button>}
          <button className="btn ghost" onClick={onClose}>{t('close')}</button>
        </div>
      </div>
    </div>
  );
}
