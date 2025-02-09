package music.service;

import music.dto.request.AlbumRequest;
import music.dto.request.UpdateAlbumRequest;
import music.dto.response.AlbumResponse;
import music.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AlbumService {
    long addAlbum(AlbumRequest albumRequest, MultipartFile imageFile) throws IOException;
    AlbumResponse getAlbum(Long id);
    long updateAlbum(Long id, UpdateAlbumRequest updateAlbumRequest, MultipartFile imageFile) throws IOException;
    long deleteAlbum(Long id);
    PageResponse<?> getAllAlbums(int pageNo, int pageSize, String sortBy);
    PageResponse<?> getAlbumsByName(int pageNo, int pageSize, String sortBy, String name);
    PageResponse<?> sortAndSpecificationSearch(int pageNo, int pageSize, String sortBy, String search);
}
