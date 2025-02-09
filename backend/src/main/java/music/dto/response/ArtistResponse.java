package music.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class ArtistResponse implements Serializable {
    private String name;
    private String bio;
    private String imageUrl;
    private Long followers;
    private List<SongResponse> songs;
    private List<Long> albumIds;
    private List<String> albumNames;
    private List<String> albumImageUrls;
}
