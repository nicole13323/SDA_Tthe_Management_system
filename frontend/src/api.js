const BASE = '/api';
let token = localStorage.getItem('token');

export function setToken(t) {
  token = t;
  if (t) localStorage.setItem('token', t);
  else localStorage.removeItem('token');
}

export async function api(path, { method = 'GET', body, params } = {}) {
  const qs = params
    ? '?' + new URLSearchParams(Object.entries(params).filter(([, v]) => v !== undefined && v !== null && v !== ''))
    : '';
  const res = await fetch(BASE + path + qs, {
    method,
    headers: { 'Content-Type': 'application/json', ...(token && !path.startsWith('/auth') ? { Authorization: `Bearer ${token}` } : {}) },
    body: body ? JSON.stringify(body) : undefined
  });
  if (res.status === 401 && token) {
    setToken(null);
    localStorage.removeItem('user');
    window.location.href = '/login';
  }
  const data = await res.json().catch(() => null);
  if (!res.ok) throw new Error(data?.error || 'error');
  return data;
}
