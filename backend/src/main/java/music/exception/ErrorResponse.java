package music.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private Date timestamp;
    private int status;
    private String error;
    private String path;
    private String message;
}

