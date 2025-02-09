package music.dto.response;

import music.constant.GenreName;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class GenreResponse implements Serializable {
    private GenreName name;
    private List<Long> songIds;
    private List<String> songNames;
}
