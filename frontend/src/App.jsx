import { useState, useEffect } from 'react';
import { Routes, Route, useNavigate } from 'react-router';
import NavBar from './NavBar.jsx';
import Home from './Home.jsx';
import MagazineList from './MagazineList.jsx';
import AddBookForm from './AddBookForm.jsx';
import BookList from './BookList.jsx';
import LaptopForm from './LaptopForm.jsx';
import LaptopList from './LaptopList.jsx';
import './App.css';

// App owns the Book and Laptop lists, because each list is used by two routes (list and add form)
export default function App() {
  const navigate = useNavigate();

  // Books
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Laptops
  const [laptops, setLaptops] = useState([]);
  const [laptopsLoading, setLaptopsLoading] = useState(true);
  const [laptopsError, setLaptopsError] = useState('');

  // Load the books once, when App appears on the screen
  useEffect(() => {
    let active = true;
    fetch('/api/rest/books')
      .then(res => {
        if (!res.ok) throw new Error(`Load failed (${res.status})`);
        return res.json();
      })
      .then(data => { if (active) setBooks(data); })
      .catch(err => { if (active) setError(err.message); })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, []);

  // Load the laptops once, in the same way
  useEffect(() => {
    let active = true;
    fetch('/api/rest/laptops')
      .then(res => {
        if (!res.ok) throw new Error(`Load failed (${res.status})`);
        return res.json();
      })
      .then(data => { if (active) setLaptops(data); })
      .catch(err => { if (active) setLaptopsError(err.message); })
      .finally(() => { if (active) setLaptopsLoading(false); });
    return () => { active = false; };
  }, []);

  // ----- Books -----
  function handleBookAdded(savedBook) {
    // New array with the saved book added at the end (never push into the old array)
    setBooks(previous => [...previous, savedBook]);
    navigate('/inventory');
  }

  async function handleDeleteBook(id) {
    if (!window.confirm('Delete this book?')) return;
    setError('');
    try {
      const response = await fetch(`/api/rest/books/${id}`, { method: 'DELETE' });
      if (!response.ok) throw new Error(`Delete failed (${response.status})`);
      // Remove the row only after the server accepted the delete
      setBooks(previous => previous.filter(book => book.id !== id));
    } catch (err) { setError(err.message); }
  }

  async function handleUpdateBook(id, data) {
    const response = await fetch(`/api/rest/books/${id}`, {
      method: 'PUT', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    if (!response.ok) throw new Error(`Update failed (${response.status})`);
    const savedBook = await response.json();
    // Replace only the book with this id by the version saved on the server
    setBooks(previous => previous.map(book => book.id === id ? savedBook : book));
  }

  // ----- Laptops -----
  function handleLaptopAdded(savedLaptop) {
    // The form already did the POST; here the saved laptop is only added to the list
    setLaptops(previous => [...previous, savedLaptop]);
    navigate('/laptops');
  }

  async function handleDeleteLaptop(id) {
    if (!window.confirm('Delete this laptop?')) return;
    setLaptopsError('');
    try {
      const response = await fetch(`/api/rest/laptops/${id}`, { method: 'DELETE' });
      if (!response.ok) throw new Error(`Delete failed (${response.status})`);
      setLaptops(previous => previous.filter(laptop => laptop.id !== id));
    } catch (err) { setLaptopsError(err.message); }
  }

  async function handleUpdateLaptop(id, data) {
    const response = await fetch(`/api/rest/laptops/${id}`, {
      method: 'PUT', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    if (!response.ok) throw new Error(`Update failed (${response.status})`);
    const savedLaptop = await response.json();
    setLaptops(previous => previous.map(laptop => laptop.id === id ? savedLaptop : laptop));
  }

  return <main className="app-container">
    <h1>My Bookstore Frontend</h1>
    <NavBar />
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/inventory" element={<BookList books={books} loading={loading} error={error}
        onDelete={handleDeleteBook} onUpdate={handleUpdateBook} />} />
      <Route path="/add" element={<AddBookForm onBookAdded={handleBookAdded} />} />
      <Route path="/magazines" element={<MagazineList />} />
      <Route path="/laptops" element={<LaptopList laptops={laptops} loading={laptopsLoading} error={laptopsError}
        onDelete={handleDeleteLaptop} onUpdate={handleUpdateLaptop} />} />
      <Route path="/laptops/add" element={<LaptopForm onLaptopAdded={handleLaptopAdded} />} />
      <Route path="*" element={<h2>Page not found</h2>} />
    </Routes>
  </main>;
}
