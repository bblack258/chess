package dataaccess;

public class AlreadyExistsException extends DataAccessException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
