package music.service;

import music.dto.request.*;
import music.dto.response.ArtistResponse;
import music.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArtistService {
    long addArtist(String artistName, UserRequest userRequest, MultipartFile imageFile) throws IOException;
    ArtistResponse getArtist(Long id);
    long updateArtist(Long id, UpdateArtistInfoRequest request, MultipartFile imageFile) throws IOException;
    long deleteArtist(Long id);
    long addMySong(SongRequest songRequest, MultipartFile imageFile, MultipartFile songFile) throws IOException;
    long addMyAlbum(AlbumRequest albumRequest, MultipartFile imageFile) throws IOException;
    long addMySongToMyAlbum(Long songId, Long albumId);
    long updateMySong(Long songId, UpdateSongRequest songRequest, MultipartFile imageFile, MultipartFile songFile) throws IOException;
    long updateMyAlbum(Long albumId, AlbumRequest albumRequest, MultipartFile imageFile) throws IOException;
    long updateMyArtistPage(UpdateArtistInfoRequest request, MultipartFile imageFile) throws IOException;
    long deleteMySong(Long songId);
    long deleteMyAlbum(Long albumId);
    long deleteMySongFromMyAlbum(Long songId, Long albumId);
    PageResponse<?> getAllArtists(int pageNo, int pageSize, String sortBy);
    PageResponse<?> getArtistsByName(int pageNo, int pageSize, String sortBy, String name);
    PageResponse<?> sortAndSpecificationSearch(int pageNo, int pageSize, String sortBy, String search);
    List<?> getTopArtists();
}
