package dataaccesserrors;

public class BadRequestException extends DataAccessException {
    public BadRequestException(String message) {
        super(message);
    }
}
