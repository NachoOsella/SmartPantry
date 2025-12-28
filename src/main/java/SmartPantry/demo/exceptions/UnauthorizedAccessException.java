package SmartPantry.demo.exceptions;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String resourceName, Long id, Long userId) {
        super(String.format("Unauthorized access to %s with id: %d by user: %d", resourceName, id, userId));
    }
}