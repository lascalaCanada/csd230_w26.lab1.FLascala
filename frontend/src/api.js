import axios from 'axios';

// One shared Axios client for every request in the app.
// baseURL means api.get('/books') calls /api/rest/books (the Vite proxy sends /api to Spring).
const api = axios.create({ baseURL: '/api/rest' });

export default api;
