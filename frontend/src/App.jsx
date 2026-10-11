import { Navigate, Route, Routes } from 'react-router-dom';
import { homeFor, useAuth } from './auth.jsx';
import Layout from './components/Layout.jsx';
import Login from './pages/Login.jsx';
import MemberDashboard from './pages/MemberDashboard.jsx';
import ChurchDashboard from './pages/ChurchDashboard.jsx';
import ManagerDashboard from './pages/ManagerDashboard.jsx';

function Protected({ role, children }) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (user.role !== role) return <Navigate to={homeFor(user.role)} replace />;
  return children;
}

export default function App() {
  const { user } = useAuth();
  return (
    <Layout>
      <Routes>
        <Route path="/login" element={user ? <Navigate to={homeFor(user.role)} replace /> : <Login />} />
        <Route path="/member" element={<Protected role="MEMBER"><MemberDashboard /></Protected>} />
        <Route path="/church" element={<Protected role="CHURCH"><ChurchDashboard /></Protected>} />
        <Route path="/manager" element={<Protected role="TITHE_MANAGER"><ManagerDashboard /></Protected>} />
        <Route path="*" element={<Navigate to={user ? homeFor(user.role) : '/login'} replace />} />
      </Routes>
    </Layout>
  );
}
