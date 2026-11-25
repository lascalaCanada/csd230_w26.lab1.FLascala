import { useContext } from 'react';
import { AuthContext } from './AuthContext.js';

// Small hook so any component can write: const { token, login, logout } = useAuth();
export default function useAuth() { return useContext(AuthContext); }
