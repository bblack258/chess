package dataaccess;

public class DoesNotExistException extends DataAccessException {
    public DoesNotExistException(String message) {
        super(message);
    }
}
