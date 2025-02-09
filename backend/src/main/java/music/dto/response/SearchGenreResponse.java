package music.dto.response;

import music.constant.GenreName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchGenreResponse {
    private Integer id;
    private GenreName name;
}
