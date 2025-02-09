package music.exception;

public class AudioUploadException extends RuntimeException {
    public AudioUploadException() {
        super();
    }

    public AudioUploadException(String message) {
        super(message);
    }

    public AudioUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public AudioUploadException(Throwable cause) {
        super(cause);
    }
}
