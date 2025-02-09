package music.exception;

public class DataInUseException extends RuntimeException {
    public DataInUseException() {
        super();
    }

    public DataInUseException(String message) {
        super(message);
    }

    public DataInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataInUseException(Throwable cause) {
        super(cause);
    }
}
