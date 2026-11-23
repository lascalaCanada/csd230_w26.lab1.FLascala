import { createContext } from 'react';

// Shared place for the login state (token, login, logout). It is filled by AuthProvider.
export const AuthContext = createContext(null);
