package music.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
public class SearchArtistResponse implements Serializable {
    private Long id;
    private String name;
    private String imageUrl;
    private long followers;
}
