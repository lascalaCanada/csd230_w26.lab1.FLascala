package csd230.lab1.exception;

/**
 * Thrown when a magazine with the given id does not exist.
 * It extends RuntimeException, so it does not need to be declared with "throws".
 */
public class MagazineNotFoundException extends RuntimeException {

    public MagazineNotFoundException(Long id) {
        super("Could not find magazine with ID: " + id);
    }
}
