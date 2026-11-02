package csd230.lab1.controller;

import csd230.lab1.entities.BookEntity;
import csd230.lab1.exception.BookNotFoundException;
import csd230.lab1.repositories.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for books. It returns JSON instead of Thymeleaf pages.
 */
@RestController
@RequestMapping("/api/rest/books")
@Tag(name = "Books", description = "CRUD operations for books")
public class BookRestController {

    private final BookRepository bookRepository;

    public BookRestController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    @Operation(summary = "List all books")
    @ApiResponse(responseCode = "200", description = "List of books returned")
    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one book by id")
    @ApiResponse(responseCode = "200", description = "Book found")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public BookEntity getBook(@PathVariable Long id) {
        // orElseThrow runs the exception only when no book has this id
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new book")
    @ApiResponse(responseCode = "201", description = "Book created")
    public BookEntity createBook(@RequestBody BookEntity newBook) {
        // Clear the id so the database always creates a new row
        newBook.setId(null);
        return bookRepository.save(newBook);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book")
    @ApiResponse(responseCode = "200", description = "Book updated")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public BookEntity updateBook(@PathVariable Long id, @RequestBody BookEntity newBook) {
        BookEntity book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        book.setTitle(newBook.getTitle());
        book.setAuthor(newBook.getAuthor());
        book.setPrice(newBook.getPrice());
        book.setCopies(newBook.getCopies());
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book")
    @ApiResponse(responseCode = "204", description = "Book deleted")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @ApiResponse(responseCode = "409", description = "Book is still used by a cart or an order")
    public void deleteBook(@PathVariable Long id) {
        // deleteById does nothing when the id is missing, so check first to return a 404
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }
}
