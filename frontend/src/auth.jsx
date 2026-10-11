import { createContext, useContext, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { api, setToken } from './api.js';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const { i18n } = useTranslation();
  const [user, setUser] = useState(() => {
    try { return JSON.parse(localStorage.getItem('user')); } catch { return null; }
  });

  async function login(identifier, password) {
    const res = await api('/auth/login', { method: 'POST', body: { identifier, password } });
    setToken(res.token);
    localStorage.setItem('user', JSON.stringify(res));
    setUser(res);
    if (res.role === 'MEMBER' && res.language) i18n.changeLanguage(res.language);
    return res;
  }

  function logout() {
    setToken(null);
    localStorage.removeItem('user');
    setUser(null);
  }

  return <AuthContext.Provider value={{ user, login, logout }}>{children}</AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext);

export const homeFor = (role) =>
  role === 'MEMBER' ? '/member' : role === 'CHURCH' ? '/church' : role === 'TITHE_MANAGER' ? '/manager' : '/login';
