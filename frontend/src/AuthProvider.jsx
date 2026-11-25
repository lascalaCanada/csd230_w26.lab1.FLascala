import { useEffect, useState } from 'react';
import { hasRole } from './tokenRoles.js';
import { AuthContext } from './AuthContext.js';

// Keeps the JWT in React state and in localStorage (so a page reload keeps the login).
// Only the token is stored: never the username or the password.
export default function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('token'));

  // The response interceptor in api.js sends 'auth:invalid' when the server rejects the token
  useEffect(() => {
    function invalidSession() { setToken(null); }
    window.addEventListener('auth:invalid', invalidSession);
    return () => window.removeEventListener('auth:invalid', invalidSession);
  }, []);

  function login(nextToken) {
    if (typeof nextToken !== 'string' || !nextToken.trim()) throw new Error('Login did not return a token.');
    // Storage is written first, so the next request already finds the new token
    localStorage.setItem('token', nextToken);
    setToken(nextToken);
  }

  function logout() {
    localStorage.removeItem('token');
    setToken(null);
  }

  // isAdmin only decides what the screen shows; the server still enforces the permissions
  return <AuthContext.Provider value={{ token, login, logout, isAdmin: hasRole(token, 'ROLE_ADMIN') }}>{children}</AuthContext.Provider>;
}
