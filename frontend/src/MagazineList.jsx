import useAuth from './useAuth.js';
import api from './api.js';
import { useState, useEffect } from 'react';
import MagazineForm from './MagazineForm.jsx';
import Magazine from './Magazine.jsx';

// Owns the magazine list because the form and the rows live on the same page
export default function MagazineList() {
  const { token } = useAuth();
  // True only while the login that opened this page is still the active one
  const isCurrent = () => localStorage.getItem('token') === token;
  const [magazines, setMagazines] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Load the magazines once, when the component appears on the screen
  useEffect(() => {
    let active = true;
    api.get('/magazines')
      .then(response => { if (active && localStorage.getItem('token') === token) setMagazines(response.data); })
      .catch(err => { if (active && localStorage.getItem('token') === token) setError(err.message); })
      .finally(() => { if (active && localStorage.getItem('token') === token) setLoading(false); });
    // Cleanup: an old response must not update a component that is gone
    return () => { active = false; };
  }, [token]);

  async function handleDeleteMagazine(id) {
    if (!window.confirm('Delete this magazine?')) return;
    setError('');
    try {
      await api.delete(`/magazines/${id}`);
      if (!isCurrent()) return;
      // The successful DELETE has no body, so there is no res.json() here.
      // filter builds a new array without the deleted magazine
      setMagazines(previous => previous.filter(magazine => magazine.id !== id));
    } catch (err) { if (isCurrent()) setError(err.message); }
  }

  async function handleUpdateMagazine(id, data) {
    const res = await api.put(`/magazines/${id}`, data);
    if (!isCurrent()) return;
    const saved = res.data;
    // map builds a new array, replacing only the magazine with this id
    setMagazines(previous => previous.map(magazine => magazine.id === id ? saved : magazine));
  }

  return <section>
    <h1>Magazines</h1>
    <MagazineForm onMagazineAdded={saved => { if (isCurrent()) setMagazines(previous => [...previous, saved]); }} />
    {loading && <p>Loading magazines...</p>}
    {error && <p role="alert">{error}</p>}
    {!loading && !error && magazines.length === 0 && <p>No magazines yet.</p>}
    <div className="magazine-list">
      {magazines.map(magazine => <Magazine key={magazine.id} {...magazine}
        onUpdate={handleUpdateMagazine} onDelete={handleDeleteMagazine} />)}
    </div>
  </section>;
}
