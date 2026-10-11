import { useTranslation } from 'react-i18next';
import { dateTime, money } from '../format.js';

export default function PaymentsTable({ items, showMember = false, showChurch = false, onReceipt }) {
  const { t, i18n } = useTranslation();
  const lng = i18n.resolvedLanguage;
  if (!items || items.length === 0) return <p className="muted">{t('empty')}</p>;
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th>{t('date')}</th>
            {showMember && <th>{t('member')}</th>}
            {showChurch && <th>{t('church')}</th>}
            <th>{t('method')}</th>
            <th className="num">{t('amount')}</th>
            <th>{t('receipt')}</th>
          </tr>
        </thead>
        <tbody>
          {items.map((p) => (
            <tr key={p.id}>
              <td>{dateTime(p.paidAt, lng)}</td>
              {showMember && <td>{p.memberName}</td>}
              {showChurch && <td>{p.churchName}</td>}
              <td>{t(`methods.${p.method}`)}</td>
              <td className="num">{money(p.amount, lng)}</td>
              <td>
                {p.receiptNo
                  ? <button className="link" onClick={() => onReceipt(p.receiptNo)}>{p.receiptNo}</button>
                  : '—'}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
