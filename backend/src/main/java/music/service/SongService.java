package music.service;

import music.dto.request.SongRequest;
import music.dto.request.UpdateSongRequest;
import music.dto.response.PageResponse;
import music.dto.response.SongResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SongService {
    long addSong(SongRequest request, MultipartFile imageFile, MultipartFile songFile) throws IOException;
    SongResponse getSong(Long id);
    long updateSong(Long id, UpdateSongRequest request, MultipartFile imageFile, MultipartFile songFile) throws IOException;
    long deleteSong(Long id);
    PageResponse<?> getAllSongs(int pageNo, int pageSize, String sortBy);
    PageResponse<?> getSongsByName(int pageNo, int pageSize, String sortBy, String name);
    PageResponse<?> getSongsByGenre(int pageNo, int pageSize, String sortBy, Integer genreId);
    PageResponse<?> sortAndSpecificationSearch(int pageNo, int pageSize, String sortBy, String search);
    List<?> getFeaturedSongs();
    List<?> getMadeForYouSongs();
    List<?> getTrendingSongs();
}
