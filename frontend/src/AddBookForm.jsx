import errorText from './errorText.js';
import useAuth from './useAuth.js';
import api from './api.js';
import { useState } from 'react';

// Controlled form: every input value lives in React state (the draft of a new book)
export default function AddBookForm({ onBookAdded }) {
  const { token } = useAuth();
  // True only while the same login that opened this form is still active
  const isCurrent = () => localStorage.getItem('token') === token;
  const [title, setTitle] = useState('');
  const [author, setAuthor] = useState('');
  const [price, setPrice] = useState('');
  const [copies, setCopies] = useState(10);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  async function handleSubmit(event) {
    // Stop the browser from reloading the page when the form is submitted
    event.preventDefault();
    setError(''); setSaving(true);
    try {
      // Input values are strings, so price and copies are converted to numbers for the backend
      const response = await api.post('/books', {
        title, author, price: Number(price), copies: Number(copies)
      });
      // A response that arrives after logout or after another login is ignored
      if (!isCurrent()) return;
      const savedBook = response.data;
      // Tell the parent (App) about the saved book, which already has its id
      onBookAdded(savedBook);
      // Clear the form only after a successful save, so a failed save keeps the typed values
      setTitle(''); setAuthor(''); setPrice(''); setCopies(10);
    } catch (err) { if (isCurrent()) setError(errorText(err)); }
    finally { if (isCurrent()) setSaving(false); }
  }

  return <form onSubmit={handleSubmit}>
    <h2>Add New Book</h2>
    <label>Title<input value={title} onChange={e => setTitle(e.target.value)} required /></label>
    <label>Author<input value={author} onChange={e => setAuthor(e.target.value)} required /></label>
    <label>Price<input type="number" min="0" step="0.01" value={price} onChange={e => setPrice(e.target.value)} required /></label>
    <label>Copies<input type="number" min="0" step="1" value={copies} onChange={e => setCopies(e.target.value)} required /></label>
    <button disabled={saving}>Save Book</button>
    {error && <p role="alert">{error}</p>}
  </form>;
}
