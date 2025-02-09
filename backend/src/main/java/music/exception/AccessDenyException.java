package music.exception;

public class AccessDenyException extends RuntimeException {
    public AccessDenyException() {
        super();
    }

    public AccessDenyException(String message) {
        super(message);
    }

    public AccessDenyException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDenyException(Throwable cause) {
        super(cause);
    }
}

