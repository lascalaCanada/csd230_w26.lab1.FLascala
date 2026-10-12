package csd230.lab1.controller;

import csd230.lab1.entities.BookEntity;
import csd230.lab1.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller class handling HTTP requests for viewing available books.
 * The add, edit and delete pages are restricted to admins in WebSecurityConfig.
 */
@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Renders the list of books available in the database.
     */
    @GetMapping
    public String listBooks(Model model) {
        List<BookEntity> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    /**
     * Shows the empty form to create a book.
     */
    @GetMapping("/add")
    public String showAddForm() {
        return "bookAdd";
    }

    /**
     * Receives the form and saves the new book.
     */
    @PostMapping("/add")
    public String addBook(@RequestParam("title") String title,
                          @RequestParam("author") String author,
                          @RequestParam("price") double price,
                          @RequestParam("copies") int copies) {
        bookRepository.save(new BookEntity(title, price, copies, author));
        return "redirect:/books";
    }

    /**
     * Shows the form already filled with the data of the book being edited.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        BookEntity book = bookRepository.findById(id).orElse(null);

        // Unknown id: just go back to the list
        if (book == null) {
            return "redirect:/books";
        }

        model.addAttribute("book", book);
        return "bookEdit";
    }

    /**
     * Receives the edited data and updates the existing book.
     */
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") Long id,
                             @RequestParam("title") String title,
                             @RequestParam("author") String author,
                             @RequestParam("price") double price,
                             @RequestParam("copies") int copies) {
        BookEntity book = bookRepository.findById(id).orElse(null);

        if (book != null) {
            book.setTitle(title);
            book.setAuthor(author);
            book.setPrice(price);
            book.setCopies(copies);
            bookRepository.save(book);
        }

        return "redirect:/books";
    }

    /**
     * Deletes a book. It has to be a POST, never a plain link.
     */
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            bookRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // The book is still referenced by a cart or an order, so the database refuses to delete it.
            // The flash attribute survives the redirect and is shown once on the book list.
            redirectAttributes.addFlashAttribute("error",
                    "This book cannot be deleted because it is in a cart or in an order.");
        }
        return "redirect:/books";
    }
}