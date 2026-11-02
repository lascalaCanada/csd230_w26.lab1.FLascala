package csd230.lab1.exception;

/**
 * Thrown when a book with the given id does not exist.
 * It extends RuntimeException, so it does not need to be declared with "throws".
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Could not find book with ID: " + id);
    }
}
