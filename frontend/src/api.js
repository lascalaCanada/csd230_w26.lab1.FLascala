import axios from 'axios';

// One shared Axios client for every request in the app.
// baseURL means api.get('/books') calls /api/rest/books (the Vite proxy sends /api to Spring).
const api = axios.create({ baseURL: '/api/rest' });

// Request interceptor: runs just before each request leaves the browser.
// The token is read from localStorage now (not when this file loads), so a new login works at once.
api.interceptors.request.use(config => {
  const token = globalThis.localStorage?.getItem('token');
  // The login request must never carry an old (maybe expired) token
  config.authToken = config.url === '/auth/login' ? null : token;
  config.headers.delete('Authorization');
  if (config.authToken) config.headers.set('Authorization', `Bearer ${config.authToken}`);
  return config;
});

// Response interceptor: a protected 401 means the token is missing, invalid or expired.
// A 403 only means "no permission" for that account, so it never logs the user out.
let redirecting = false;
api.interceptors.response.use(response => response, error => {
  const requestToken = error.config?.authToken;
  // Only a request sent with the CURRENT token can end the session,
  // so a late 401 from an old token cannot sign out a newer login
  if (error.response?.status === 401 && requestToken &&
      error.config.url !== '/auth/login' &&
      requestToken === globalThis.localStorage?.getItem('token')) {
    localStorage.removeItem('token');
    // Tells AuthProvider to drop its token state
    window.dispatchEvent(new Event('auth:invalid'));
    // One full-page navigation, even when several requests fail together
    if (!redirecting && window.location.pathname !== '/login') {
      redirecting = true;
      window.location.replace('/login?reauth=true');
    }
  }
  return Promise.reject(error); // 403 is forbidden, not a logout.
});

export default api;
