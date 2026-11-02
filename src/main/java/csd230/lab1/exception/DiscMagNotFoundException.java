package csd230.lab1.exception;

/**
 * Thrown when a disc magazine with the given id does not exist.
 * It extends RuntimeException, so it does not need to be declared with "throws".
 */
public class DiscMagNotFoundException extends RuntimeException {

    public DiscMagNotFoundException(Long id) {
        super("Could not find disc magazine with ID: " + id);
    }
}
