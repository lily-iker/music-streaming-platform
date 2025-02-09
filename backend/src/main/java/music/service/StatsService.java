package music.service;

import lombok.RequiredArgsConstructor;
import music.dto.response.StatsResponse;
import music.repository.AlbumRepository;
import music.repository.ArtistRepository;
import music.repository.SongRepository;
import music.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;

    public StatsResponse getStats() {
        return StatsResponse.builder()
                .totalSongs(songRepository.count())
                .totalAlbums(albumRepository.count())
                .totalArtists(artistRepository.count())
                .totalUsers(userRepository.count())
                .build();
    }
}
