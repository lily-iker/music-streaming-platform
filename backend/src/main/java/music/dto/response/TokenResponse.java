package music.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class TokenResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
    private Long userId;
}