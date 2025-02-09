package music.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class SearchSongResponse implements Serializable {
    private Long id;
    private String name;
    private Integer duration;
    private String imageUrl;
    private String songUrl;
    private Long likeCount;
    private List<Long> artistIds;
    private List<String> artistNames;
}
