import { useState } from 'react';

// One laptop row. It can show the laptop or an inline edit form (same idea as Book.jsx)
export default function Laptop({ laptop, onDelete, onUpdate }) {
  const [isEditing, setIsEditing] = useState(false);
  const [draft, setDraft] = useState(laptop);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  function beginEdit() {
    setDraft(laptop);
    setError(''); setIsEditing(true);
  }

  function handleChange(event) {
    const { name, value } = event.target;
    setDraft(previous => ({ ...previous, [name]: value }));
  }

  async function handleSave(event) {
    event.preventDefault(); setError(''); setSaving(true);
    try {
      // The spread keeps every field of the saved laptop; the number inputs are converted back to numbers
      await onUpdate(laptop.id, { ...draft, price: Number(draft.price), copies: Number(draft.copies),
        ramGb: Number(draft.ramGb), storageGb: Number(draft.storageGb) });
      setIsEditing(false);
    } catch (err) { setError(err.message); }
    finally { setSaving(false); }
  }

  if (isEditing) return <form className="laptop-row editing" onSubmit={handleSave}>
    {/* ?? '' avoids a React warning when a text field is null */}
    <label>Brand<input name="brand" value={draft.brand ?? ''} onChange={handleChange} required /></label>
    <label>Description<input name="description" value={draft.description ?? ''} onChange={handleChange} required /></label>
    <label>Processor<input name="processor" value={draft.processor ?? ''} onChange={handleChange} required /></label>
    <label>Price<input name="price" type="number" min="0" step="0.01" value={draft.price} onChange={handleChange} required /></label>
    <label>Copies<input name="copies" type="number" min="0" step="1" value={draft.copies} onChange={handleChange} required /></label>
    <label>RAM (GB)<input name="ramGb" type="number" min="0" step="1" value={draft.ramGb} onChange={handleChange} required /></label>
    <label>Storage (GB)<input name="storageGb" type="number" min="0" step="1" value={draft.storageGb} onChange={handleChange} required /></label>
    <button disabled={saving}>Save</button>
    <button type="button" disabled={saving} onClick={() => setIsEditing(false)}>Cancel</button>
    {error && <p role="alert">{error}</p>}
  </form>;

  return <article className="laptop-row">
    <h3>{laptop.brand} - {laptop.description}</h3>
    <p>ID: {laptop.id} | Processor: {laptop.processor} | RAM: {laptop.ramGb} GB | Storage: {laptop.storageGb} GB</p>
    <p>Price: ${Number(laptop.price).toFixed(2)} | Copies: {laptop.copies}</p>
    <button onClick={beginEdit}>Edit</button>
    <button onClick={() => onDelete(laptop.id)}>Delete</button>
  </article>;
}
