package music.controller;

import music.dto.request.AlbumRequest;
import music.dto.request.UpdateAlbumRequest;
import music.dto.response.AlbumResponse;
import music.dto.response.ApiResponse;
import music.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/album")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> addAlbum(@Valid @RequestPart AlbumRequest albumRequest,
                                      @RequestPart MultipartFile imageFile) throws IOException {
        return new ApiResponse<>(HttpStatus.CREATED.value(),
                "Add album success",
                albumService.addAlbum(albumRequest, imageFile));
    }

    @GetMapping("/{albumId}")
    public ApiResponse<AlbumResponse> getAlbum(@PathVariable Long albumId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get album success",
                albumService.getAlbum(albumId));
    }

    @PutMapping("/{albumId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> updateAlbum(@PathVariable Long albumId,
                                         @Valid @RequestPart UpdateAlbumRequest updateAlbumRequest,
                                         @RequestPart(required = false) MultipartFile imageFile) throws IOException {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Update album success",
                albumService.updateAlbum(albumId, updateAlbumRequest, imageFile));
    }

    @DeleteMapping("/{albumId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> deleteAlbum(@PathVariable Long albumId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Delete album success",
                albumService.deleteAlbum(albumId));
    }

    @GetMapping("/all")
    public ApiResponse<?> getAllAlbums(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "id:desc", required = false) String sortBy) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get all albums success",
                albumService.getAllAlbums(pageNo, pageSize, sortBy));
    }

    @GetMapping("/find-by-name")
    public ApiResponse<?> getAlbumsByName(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "id:desc", required = false) String sortBy,
            @RequestParam String albumName) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get albums by name success",
                albumService.getAlbumsByName(pageNo, pageSize, sortBy, albumName));
    }

    @GetMapping("/specification")
    public ApiResponse<?> sortAndSpecificationSearch(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "id:desc", required = false) String sortBy,
            @RequestParam(defaultValue = "id!=0", required = false) String search) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Specification Search",
                albumService.sortAndSpecificationSearch(pageNo, pageSize, sortBy, search));
    }
}