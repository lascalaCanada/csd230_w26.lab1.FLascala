package csd230.lab1.exception;

/**
 * Thrown when a laptop with the given id does not exist.
 * It extends RuntimeException, so it does not need to be declared with "throws".
 */
public class LaptopNotFoundException extends RuntimeException {

    public LaptopNotFoundException(Long id) {
        super("Could not find laptop with ID: " + id);
    }
}
