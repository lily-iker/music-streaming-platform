package com.example.demo.controller;

import com.example.demo.dto.request.AlbumRequestForArtist;
import com.example.demo.dto.request.SongRequestForArtist;
import com.example.demo.dto.request.UpdateArtistInfoRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.ArtistResponse;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/artist")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<Long> addArtist(@RequestParam String artistName,
                                        @Valid @RequestBody UserRequest userRequest) {
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Add artist success",
                artistService.addArtist(artistName, userRequest));
    }

    @GetMapping("/{artistId}")
    public ResponseData<ArtistResponse> getArtist(@PathVariable Long artistId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get artist success",
                artistService.getArtist(artistId));
    }

    @PutMapping("/{artistId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<Long> updateArtist(@PathVariable Long artistId,
                                           @Valid @RequestPart UpdateArtistInfoRequest request,
                                           @RequestPart MultipartFile imageFile) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Update artist success",
                artistService.updateArtist(artistId, request, imageFile));
    }

    @DeleteMapping("/{artistId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<Long> deleteArtist(@PathVariable Long artistId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Delete artist success",
                artistService.deleteArtist(artistId));
    }

    @PostMapping("/add-my-song")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseData<Long> addMySong(@Valid @RequestPart SongRequestForArtist songRequest,
                                        @RequestPart MultipartFile imageFile,
                                        @RequestPart MultipartFile songFile) throws IOException {
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Add song to artist success",
                artistService.addMySong(songRequest, imageFile, songFile));
    }

    @PostMapping("/add-my-album")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseData<Long> addMyAlbum(@Valid @RequestPart AlbumRequestForArtist albumRequest,
                                         @RequestPart MultipartFile imageFile) {
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Album added successfully",
                artistService.addMyAlbum(albumRequest, imageFile));
    }

    @PostMapping("/add-song-to-album")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseData<Long> addMySongToMyAlbum(@RequestParam Long songId,
                                                 @RequestParam Long albumId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Song added to album successfully",
                artistService.addMySongToMyAlbum(songId, albumId));
    }

    @PutMapping("/song/{songId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseData<Long> updateMySong(@PathVariable Long songId,
                                           @Valid @RequestPart SongRequestForArtist songRequest,
                                           @RequestPart MultipartFile imageFile,
                                           @RequestPart MultipartFile songFile) throws IOException {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Update song of artist success",
                artistService.updateMySong(songId, songRequest, imageFile, songFile));
    }

    @PutMapping("/album/{albumId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseData<Long> updateMyAlbum(@PathVariable Long albumId,
                                            @Valid @RequestPart AlbumRequestForArtist albumRequest,
                                            @RequestPart MultipartFile imageFile) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Album updated successfully",
                artistService.updateMyAlbum(albumId, albumRequest, imageFile));
    }

    @PutMapping("/update-my-artist-page")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseData<Long> updateMyArtistPage(@Valid @RequestPart UpdateArtistInfoRequest request,
                                                 @RequestPart MultipartFile imageFile) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Update artist page success",
                artistService.updateMyArtistPage(request, imageFile));
    }

    @DeleteMapping("/song/{songId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseData<Long> deleteMySong(@PathVariable Long songId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Delete song of artist success",
                artistService.deleteMySong(songId));
    }

    @DeleteMapping("/album/{albumId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseData<Long> deleteMyAlbum(@PathVariable Long albumId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Album deleted successfully",
                artistService.deleteMyAlbum(albumId));
    }

    @DeleteMapping("/song-from-album")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseData<Long> deleteMySongFromMyAlbum(@RequestParam Long songId,
                                                      @RequestParam Long albumId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Song removed from album successfully",
                artistService.deleteMySongFromMyAlbum(songId, albumId));
    }

    @GetMapping("/all")
    public ResponseData<?> getAllArtists(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "followers:desc", required = false) String sortBy) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get all artists success",
                artistService.getAllArtists(pageNo, pageSize, sortBy));
    }

    @GetMapping("/find-by-name")
    public ResponseData<?> getArtistsByName(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "followers:desc", required = false) String sortBy,
            @RequestParam String name) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get artists by name success",
                artistService.getArtistsByName(pageNo, pageSize, sortBy, name));
    }

    @GetMapping("/specification")
    public ResponseData<?> sortAndSpecificationSearch(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "followers:desc", required = false) String sortBy,
            @RequestParam(defaultValue = "id!=0", required = false) String search) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Specification Search",
                artistService.sortAndSpecificationSearch(pageNo, pageSize, sortBy, search));
    }
}
