package music.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class StatsResponse implements Serializable {
    private Long totalSongs;
    private Long totalAlbums;
    private Long totalArtists;
    private Long totalUsers;
}
