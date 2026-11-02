package csd230.lab1.exception;

/**
 * Thrown when a smartphone with the given id does not exist.
 * It extends RuntimeException, so it does not need to be declared with "throws".
 */
public class SmartPhoneNotFoundException extends RuntimeException {

    public SmartPhoneNotFoundException(Long id) {
        super("Could not find smartphone with ID: " + id);
    }
}
