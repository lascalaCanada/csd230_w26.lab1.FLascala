package csd230.lab1.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handling for the REST controllers.
 * Each handler catches one exception type and sends the message back as the response body.
 * Only controllers marked with @RestController are covered, so the MVC pages are not affected.
 */
@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionAdvice {

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String bookNotFoundHandler(BookNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MagazineNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String magazineNotFoundHandler(MagazineNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DiscMagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String discMagNotFoundHandler(DiscMagNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(TicketNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String ticketNotFoundHandler(TicketNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(LaptopNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String laptopNotFoundHandler(LaptopNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(SmartPhoneNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String smartPhoneNotFoundHandler(SmartPhoneNotFoundException ex) {
        return ex.getMessage();
    }

    // Happens when a product cannot be deleted because a cart or an order still uses it
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String dataIntegrityHandler(DataIntegrityViolationException ex) {
        return "This item is still used by a cart or an order, so it cannot be deleted";
    }
}
