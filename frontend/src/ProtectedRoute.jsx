import { Navigate, Outlet } from 'react-router';
import useAuth from './useAuth.js';

// Wraps the signed-in pages: without a token the visitor goes to /login.
// This only checks that a token exists; Spring still validates it on every request.
export default function ProtectedRoute() {
  const { token } = useAuth();
  return token ? <Outlet /> : <Navigate to="/login" replace />;
}
