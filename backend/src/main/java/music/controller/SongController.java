package music.controller;

import music.dto.request.SongRequest;
import music.dto.request.UpdateSongRequest;
import music.dto.response.ApiResponse;
import music.dto.response.SongResponse;
import music.service.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/song")
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> addSong(@Valid @RequestPart SongRequest request,
                                     @RequestPart MultipartFile imageFile,
                                     @RequestPart MultipartFile songFile) throws IOException {
        return new ApiResponse<>(HttpStatus.CREATED.value(),
                "Add song success",
                songService.addSong(request, imageFile, songFile));
    }

    @GetMapping("/{songId}")
    public ApiResponse<SongResponse> getSong(@PathVariable Long songId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get song success",
                songService.getSong(songId));
    }

    @PutMapping("/{songId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> updateSong(@PathVariable Long songId,
                                        @Valid @RequestPart UpdateSongRequest request,
                                        @RequestPart(required = false) MultipartFile imageFile,
                                        @RequestPart(required = false) MultipartFile songFile) throws IOException {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Update song success",
                songService.updateSong(songId, request, imageFile, songFile));
    }

    @DeleteMapping("/{songId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> deleteSong(@PathVariable Long songId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Delete song success",
                songService.deleteSong(songId));
    }

    @GetMapping("/all")
    public ApiResponse<?> getAllSongs(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "likeCount:desc", required = false) String sortBy) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get all songs success",
                songService.getAllSongs(pageNo, pageSize, sortBy));
    }

    @GetMapping("/find-by-name")
    public ApiResponse<?> getSongsByName(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "likeCount:desc", required = false) String sortBy,
            @RequestParam String songName) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get songs by name success",
                songService.getSongsByName(pageNo, pageSize, sortBy, songName));
    }

    @GetMapping("/find-by-genre")
    public ApiResponse<?> getSongsByGenre(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "likeCount:desc", required = false) String sortBy,
            @RequestParam Integer genreId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get songs by genre success",
                songService.getSongsByGenre(pageNo, pageSize, sortBy, genreId));
    }

    @GetMapping("/specification")
    public ApiResponse<?> sortAndSpecificationSearch(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "likeCount:desc", required = false) String sortBy,
            @RequestParam(defaultValue = "id!=0", required = false) String search) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Specification Search",
                songService.sortAndSpecificationSearch(pageNo, pageSize, sortBy, search));
    }

    @GetMapping("/featured-songs")
    public ApiResponse<?> getFeaturedSongs() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get Featured Songs success",
                songService.getFeaturedSongs());
    }

    @GetMapping("/made-for-you-songs")
    public ApiResponse<?> getMadeForYouSongs() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get Made For You Songs success",
                songService.getMadeForYouSongs());
    }

    @GetMapping("/trending-songs")
    public ApiResponse<?> getTrendingSongs() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get Trending Songs success",
                songService.getTrendingSongs());
    }
}
