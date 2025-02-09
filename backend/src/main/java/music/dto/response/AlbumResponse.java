package music.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class AlbumResponse implements Serializable {
    private String name;
    private String imageUrl;
    private Long artistId;
    private String artistName;
    private List<SongResponse> songs;
}
