import Book from './Book.jsx';

// Only displays the books it receives from App (App owns the list state)
export default function BookList({ books, loading, error, onDelete, onUpdate }) {
  if (loading) return <p>Loading books...</p>;
  return <section aria-label="Book inventory">
    <h2>Books</h2>
    {error && <p role="alert">{error}</p>}
    {!error && books.length === 0 && <p>No books yet.</p>}
    {/* The database id is a stable key, so React can match each row between renders */}
    {books.map(book => <Book key={book.id} book={book} onDelete={onDelete} onUpdate={onUpdate} />)}
  </section>;
}
