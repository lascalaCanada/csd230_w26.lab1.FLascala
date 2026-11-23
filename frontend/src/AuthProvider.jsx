import { useState } from 'react';
import { AuthContext } from './AuthContext.js';

// Keeps the JWT in React state and in localStorage (so a page reload keeps the login).
// Only the token is stored: never the username or the password.
export default function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('token'));

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

  return <AuthContext.Provider value={{ token, login, logout }}>{children}</AuthContext.Provider>;
}
