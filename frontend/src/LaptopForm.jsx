import { useState } from 'react';

// Controlled form for a new laptop. Laptop has no title: its name is the "description" field
export default function LaptopForm({ onLaptopAdded }) {
  const [brand, setBrand] = useState('');
  const [description, setDescription] = useState('');
  const [price, setPrice] = useState('');
  const [copies, setCopies] = useState(1);
  const [ramGb, setRamGb] = useState(8);
  const [storageGb, setStorageGb] = useState(256);
  const [processor, setProcessor] = useState('');
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault(); setError(''); setSaving(true);
    try {
      // The JSON names match the Java entity: brand, description, price, copies, ramGb, storageGb, processor
      const response = await fetch('/api/rest/laptops', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ brand, description, price: Number(price), copies: Number(copies),
          ramGb: Number(ramGb), storageGb: Number(storageGb), processor })
      });
      if (!response.ok) throw new Error(`Save failed (${response.status})`);
      // The saved laptop (with its new id) goes to App, which adds it to the list
      onLaptopAdded(await response.json());
      setBrand(''); setDescription(''); setPrice(''); setCopies(1);
      setRamGb(8); setStorageGb(256); setProcessor('');
    } catch (err) { setError(err.message); }
    finally { setSaving(false); }
  }

  return <form onSubmit={handleSubmit}>
    <h2>Add New Laptop</h2>
    <label>Brand<input value={brand} onChange={e => setBrand(e.target.value)} required /></label>
    <label>Description<input value={description} onChange={e => setDescription(e.target.value)} required /></label>
    <label>Processor<input value={processor} onChange={e => setProcessor(e.target.value)} required /></label>
    <label>Price<input type="number" min="0" step="0.01" value={price} onChange={e => setPrice(e.target.value)} required /></label>
    <label>Copies<input type="number" min="0" step="1" value={copies} onChange={e => setCopies(e.target.value)} required /></label>
    <label>RAM (GB)<input type="number" min="0" step="1" value={ramGb} onChange={e => setRamGb(e.target.value)} required /></label>
    <label>Storage (GB)<input type="number" min="0" step="1" value={storageGb} onChange={e => setStorageGb(e.target.value)} required /></label>
    <button disabled={saving}>Save Laptop</button>
    {error && <p role="alert">{error}</p>}
  </form>;
}
