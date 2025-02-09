package music.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class SearchAlbumResponse implements Serializable {
    private Long id;
    private String name;
    private String imageUrl;
    private Long artistId;
    private String artistName;
}
