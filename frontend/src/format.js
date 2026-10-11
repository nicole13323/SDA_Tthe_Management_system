export const money = (n, lng) =>
  new Intl.NumberFormat(lng, { style: 'currency', currency: 'RWF', maximumFractionDigits: 0 }).format(n);

export const dateTime = (iso, lng) =>
  new Intl.DateTimeFormat(lng, { dateStyle: 'medium', timeStyle: 'short' }).format(new Date(iso));

export const dateOnly = (iso, lng) =>
  new Intl.DateTimeFormat(lng, { dateStyle: 'medium' }).format(new Date(iso));
