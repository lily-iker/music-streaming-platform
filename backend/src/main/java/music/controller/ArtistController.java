package music.controller;

import music.dto.request.*;
import music.dto.response.ArtistResponse;
import music.dto.response.ApiResponse;
import music.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/artist")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> addArtist(@RequestParam String artistName,
                                       @Valid @RequestPart UserRequest userRequest,
                                       @RequestPart MultipartFile imageFile) throws IOException {
        return new ApiResponse<>(HttpStatus.CREATED.value(),
                "Add artist success",
                artistService.addArtist(artistName, userRequest, imageFile));
    }

    @GetMapping("/{artistId}")
    public ApiResponse<ArtistResponse> getArtist(@PathVariable Long artistId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get artist success",
                artistService.getArtist(artistId));
    }

    @PutMapping("/{artistId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> updateArtist(@PathVariable Long artistId,
                                          @Valid @RequestPart UpdateArtistInfoRequest request,
                                          @RequestPart MultipartFile imageFile) throws IOException {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Update artist success",
                artistService.updateArtist(artistId, request, imageFile));
    }

    @DeleteMapping("/{artistId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> deleteArtist(@PathVariable Long artistId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Delete artist success",
                artistService.deleteArtist(artistId));
    }

    @PostMapping("/add-my-song")
    @PreAuthorize("hasRole('ARTIST')")
    public ApiResponse<Long> addMySong(@Valid @RequestPart SongRequest songRequest,
                                       @RequestPart MultipartFile imageFile,
                                       @RequestPart MultipartFile songFile) throws IOException {
        return new ApiResponse<>(HttpStatus.CREATED.value(),
                "Add song to artist success",
                artistService.addMySong(songRequest, imageFile, songFile));
    }

    @PostMapping("/add-my-album")
    @PreAuthorize("hasRole('ARTIST')")
    public ApiResponse<Long> addMyAlbum(@Valid @RequestPart AlbumRequest albumRequest,
                                        @RequestPart MultipartFile imageFile) throws IOException {
        return new ApiResponse<>(HttpStatus.CREATED.value(),
                "Album added successfully",
                artistService.addMyAlbum(albumRequest, imageFile));
    }

    @PostMapping("/add-song-to-album")
    @PreAuthorize("hasRole('ARTIST')")
    public ApiResponse<Long> addMySongToMyAlbum(@RequestParam Long songId,
                                                @RequestParam Long albumId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Song added to album successfully",
                artistService.addMySongToMyAlbum(songId, albumId));
    }

    @PutMapping("/song/{songId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ApiResponse<Long> updateMySong(@PathVariable Long songId,
                                          @Valid @RequestPart UpdateSongRequest songUpdateRequest,
                                          @RequestPart MultipartFile imageFile,
                                          @RequestPart MultipartFile songFile) throws IOException {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Update song of artist success",
                artistService.updateMySong(songId, songUpdateRequest, imageFile, songFile));
    }

    @PutMapping("/album/{albumId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ApiResponse<Long> updateMyAlbum(@PathVariable Long albumId,
                                           @Valid @RequestPart AlbumRequest albumRequest,
                                           @RequestPart MultipartFile imageFile) throws IOException {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Album updated successfully",
                artistService.updateMyAlbum(albumId, albumRequest, imageFile));
    }

    @PutMapping("/update-my-artist-page")
    @PreAuthorize("hasRole('ARTIST')")
    public ApiResponse<Long> updateMyArtistPage(@Valid @RequestPart UpdateArtistInfoRequest request,
                                                @RequestPart MultipartFile imageFile) throws IOException {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Update artist page success",
                artistService.updateMyArtistPage(request, imageFile));
    }

    @DeleteMapping("/song/{songId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ApiResponse<Long> deleteMySong(@PathVariable Long songId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Delete song of artist success",
                artistService.deleteMySong(songId));
    }

    @DeleteMapping("/album/{albumId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ApiResponse<Long> deleteMyAlbum(@PathVariable Long albumId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Album deleted successfully",
                artistService.deleteMyAlbum(albumId));
    }

    @DeleteMapping("/song-from-album")
    @PreAuthorize("hasRole('ARTIST')")
    public ApiResponse<Long> deleteMySongFromMyAlbum(@RequestParam Long songId,
                                                     @RequestParam Long albumId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Song removed from album successfully",
                artistService.deleteMySongFromMyAlbum(songId, albumId));
    }

    @GetMapping("/all")
    public ApiResponse<?> getAllArtists(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "followers:desc", required = false) String sortBy) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get all artists success",
                artistService.getAllArtists(pageNo, pageSize, sortBy));
    }

    @GetMapping("/find-by-name")
    public ApiResponse<?> getArtistsByName(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "followers:desc", required = false) String sortBy,
            @RequestParam String artistName) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get artists by name success",
                artistService.getArtistsByName(pageNo, pageSize, sortBy, artistName));
    }

    @GetMapping("/specification")
    public ApiResponse<?> sortAndSpecificationSearch(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "followers:desc", required = false) String sortBy,
            @RequestParam(defaultValue = "id!=0", required = false) String search) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Specification Search",
                artistService.sortAndSpecificationSearch(pageNo, pageSize, sortBy, search));
    }

    @GetMapping("/top-artists")
    public ApiResponse<?> getTopArtists() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get artists by name success",
                artistService.getTopArtists());
    }
}
