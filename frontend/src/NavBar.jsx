import { Link, useNavigate } from 'react-router';
import useAuth from './useAuth.js';

// Link changes the URL without reloading the page. The bar stays visible because it is outside Routes
export default function NavBar() {
  const { logout } = useAuth();
  const navigate = useNavigate();
  return <nav>
    <Link to="/">Home</Link>
    <Link to="/inventory">Books</Link>
    <Link to="/add">Add Book</Link>
    <Link to="/magazines">Magazines</Link>
    <Link to="/laptops">Laptops</Link>
    <Link to="/laptops/add">Add Laptop</Link>
    {/* Logout clears the token first, then goes to the login page */}
    <button onClick={() => { logout(); navigate('/login', { replace: true }); }}>Logout</button>
  </nav>;
}
