import api from './api.js';
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
    // Axios rejects non-2xx answers by itself and gives the parsed JSON in response.data
    api.get('/books')
      .then(response => { if (active) setBooks(response.data); })
      .catch(err => { if (active) setError(err.message); })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, []);

  // Load the laptops once, in the same way
  useEffect(() => {
    let active = true;
    api.get('/laptops')
      .then(response => { if (active) setLaptops(response.data); })
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
      // A failed DELETE throws here, so the row stays; a successful one has an empty body
      await api.delete(`/books/${id}`);
      // Remove the row only after the server accepted the delete
      setBooks(previous => previous.filter(book => book.id !== id));
    } catch (err) { setError(err.message); }
  }

  async function handleUpdateBook(id, data) {
    // Axios turns the plain object into JSON by itself
    const response = await api.put(`/books/${id}`, data);
    const savedBook = response.data;
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
      await api.delete(`/laptops/${id}`);
      setLaptops(previous => previous.filter(laptop => laptop.id !== id));
    } catch (err) { setLaptopsError(err.message); }
  }

  async function handleUpdateLaptop(id, data) {
    const response = await api.put(`/laptops/${id}`, data);
    const savedLaptop = response.data;
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
