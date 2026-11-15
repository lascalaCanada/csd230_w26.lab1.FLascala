import Laptop from './Laptop.jsx';

// Only displays the laptops it receives from App (App owns the list state)
export default function LaptopList({ laptops, loading, error, onDelete, onUpdate }) {
  if (loading) return <p>Loading laptops...</p>;
  return <section aria-label="Laptop inventory">
    <h2>Laptops</h2>
    {error && <p role="alert">{error}</p>}
    {!error && laptops.length === 0 && <p>No laptops yet.</p>}
    <div className="laptop-list">
      {laptops.map(laptop => <Laptop key={laptop.id} laptop={laptop} onDelete={onDelete} onUpdate={onUpdate} />)}
    </div>
  </section>;
}
