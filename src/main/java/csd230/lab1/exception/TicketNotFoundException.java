package csd230.lab1.exception;

/**
 * Thrown when a ticket with the given id does not exist.
 * It extends RuntimeException, so it does not need to be declared with "throws".
 */
public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(Long id) {
        super("Could not find ticket with ID: " + id);
    }
}
