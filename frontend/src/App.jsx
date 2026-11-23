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
import AuthProvider from './AuthProvider.jsx';
import useAuth from './useAuth.js';
import Login from './Login.jsx';
import ProtectedRoute from './ProtectedRoute.jsx';

// App only holds the login state and the public /login page.
// Every other page is inside ProtectedRoute, so it needs a token.
export default function App() {
  return <AuthProvider><main className="app-container">
    <h1>My Bookstore Frontend</h1>
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route element={<ProtectedRoute />}><Route path="*" element={<SignedInBookApp />} /></Route>
    </Routes>
  </main></AuthProvider>;
}

// The token is the key, so a new login unmounts the old BookApp:
// the previous account's lists, drafts and pending callbacks are thrown away.
function SignedInBookApp() {
  const { token } = useAuth();
  return <BookApp key={token} />;
}

// BookApp owns the Book and Laptop lists, because each list is used by two routes (list and add form)
function BookApp() {
  const { token } = useAuth();
  // True only while the login that opened this page is still the active one
  const isCurrent = () => localStorage.getItem('token') === token;
  const navigate = useNavigate();

  // Books
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Laptops
  const [laptops, setLaptops] = useState([]);
  const [laptopsLoading, setLaptopsLoading] = useState(true);
  const [laptopsError, setLaptopsError] = useState('');

  // Load the books when the page appears (and again after a new login).
  // The active flag and the token check ignore an answer that arrives too late.
  useEffect(() => {
    let active = true;
    api.get('/books')
      .then(response => { if (active && localStorage.getItem('token') === token) setBooks(response.data); })
      .catch(err => { if (active && localStorage.getItem('token') === token) setError(err.message); })
      .finally(() => { if (active && localStorage.getItem('token') === token) setLoading(false); });
    return () => { active = false; };
  }, [token]);

  // Load the laptops in the same way
  useEffect(() => {
    let active = true;
    api.get('/laptops')
      .then(response => { if (active && localStorage.getItem('token') === token) setLaptops(response.data); })
      .catch(err => { if (active && localStorage.getItem('token') === token) setLaptopsError(err.message); })
      .finally(() => { if (active && localStorage.getItem('token') === token) setLaptopsLoading(false); });
    return () => { active = false; };
  }, [token]);

  // ----- Books -----
  function handleBookAdded(savedBook) {
    if (!isCurrent()) return;
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
      if (!isCurrent()) return;
      // Remove the row only after the server accepted the delete
      setBooks(previous => previous.filter(book => book.id !== id));
    } catch (err) { if (isCurrent()) setError(err.message); }
  }

  async function handleUpdateBook(id, data) {
    // Axios turns the plain object into JSON by itself
    const response = await api.put(`/books/${id}`, data);
    if (!isCurrent()) return;
    const savedBook = response.data;
    // Replace only the book with this id by the version saved on the server
    setBooks(previous => previous.map(book => book.id === id ? savedBook : book));
  }

  // ----- Laptops -----
  function handleLaptopAdded(savedLaptop) {
    if (!isCurrent()) return;
    // The form already did the POST; here the saved laptop is only added to the list
    setLaptops(previous => [...previous, savedLaptop]);
    navigate('/laptops');
  }

  async function handleDeleteLaptop(id) {
    if (!window.confirm('Delete this laptop?')) return;
    setLaptopsError('');
    try {
      await api.delete(`/laptops/${id}`);
      if (!isCurrent()) return;
      setLaptops(previous => previous.filter(laptop => laptop.id !== id));
    } catch (err) { if (isCurrent()) setLaptopsError(err.message); }
  }

  async function handleUpdateLaptop(id, data) {
    const response = await api.put(`/laptops/${id}`, data);
    if (!isCurrent()) return;
    const savedLaptop = response.data;
    setLaptops(previous => previous.map(laptop => laptop.id === id ? savedLaptop : laptop));
  }

  return <>
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
  </>;
}
