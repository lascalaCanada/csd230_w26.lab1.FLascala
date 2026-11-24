import { Link, useNavigate } from 'react-router';
import useAuth from './useAuth.js';

// Link changes the URL without reloading the page. The bar stays visible because it is outside Routes
export default function NavBar({ cartCount }) {
  const { logout, isAdmin } = useAuth();
  const navigate = useNavigate();
  return <nav>
    <Link to="/">Home</Link>
    <Link to="/inventory">Books</Link>
    {/* Add links are only shown to administrators */}
    {isAdmin && <Link to="/add">Add Book</Link>}
    <Link to="/magazines">Magazines</Link>
    <Link to="/laptops">Laptops</Link>
    {isAdmin && <Link to="/laptops/add">Add Laptop</Link>}
    <Link to="/cart">Cart ({cartCount})</Link>
    {/* Logout clears the token first, then goes to the login page */}
    <button onClick={() => { logout(); navigate('/login', { replace: true }); }}>Logout</button>
  </nav>;
}
