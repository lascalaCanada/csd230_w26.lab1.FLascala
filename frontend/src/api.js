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

export default api;
