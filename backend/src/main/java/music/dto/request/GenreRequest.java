package music.dto.request;

import music.constant.GenreName;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class GenreRequest implements Serializable {
    private GenreName name;
}
