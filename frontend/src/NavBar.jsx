import { Link } from 'react-router';

// Link changes the URL without reloading the page. The bar stays visible because it is outside Routes
export default function NavBar() {
  return <nav>
    <Link to="/">Home</Link>
    <Link to="/inventory">Books</Link>
    <Link to="/add">Add Book</Link>
    <Link to="/magazines">Magazines</Link>
    <Link to="/laptops">Laptops</Link>
    <Link to="/laptops/add">Add Laptop</Link>
  </nav>;
}
