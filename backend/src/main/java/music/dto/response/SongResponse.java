package music.dto.response;

import music.constant.GenreName;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class SongResponse implements Serializable {
    private Long id;
    private String name;
    private Integer duration;
    private String imageUrl;
    private String songUrl;
    private Long likeCount;
    private List<Integer> genreIds;
    private List<GenreName> genreNames;
    private List<Long> artistIds;
    private List<String> artistNames;
    private List<String> artistImageUrls;
    private Long albumId;
    private String albumName;
    private String albumImageUrl;
}
