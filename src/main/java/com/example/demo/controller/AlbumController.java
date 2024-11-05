package com.example.demo.controller;

import com.example.demo.dto.request.AlbumRequestForArtist;
import com.example.demo.dto.response.AlbumResponse;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<Long> addAlbum(@RequestParam String artistName,
                                       @Valid @RequestPart AlbumRequestForArtist albumRequest,
                                       @RequestPart MultipartFile imageFile) {
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Add album success",
                albumService.addAlbum(artistName, albumRequest, imageFile));
    }

    @GetMapping("/{albumId}")
    public ResponseData<AlbumResponse> getAlbum(@PathVariable Long albumId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get album success",
                albumService.getAlbum(albumId));
    }

    @PutMapping("/{albumId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<Long> updateAlbum(@PathVariable Long albumId,
                                          @Valid @RequestPart AlbumRequestForArtist albumRequest,
                                          @RequestPart MultipartFile imageFile) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Update album success",
                albumService.updateAlbum(albumId, albumRequest, imageFile));
    }

    @DeleteMapping("/{albumId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<Long> deleteAlbum(@PathVariable Long albumId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Delete album success",
                albumService.deleteAlbum(albumId));
    }

    @GetMapping("/all")
    public ResponseData<?> getAllAlbums(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "id:desc", required = false) String sortBy) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get all albums success",
                albumService.getAllAlbums(pageNo, pageSize, sortBy));
    }

    @GetMapping("/find-by-name")
    public ResponseData<?> getAlbumsByName(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "id:desc", required = false) String sortBy,
            @RequestParam String name) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get albums by name success",
                albumService.getAlbumsByName(pageNo, pageSize, sortBy, name));
    }

    @GetMapping("/specification")
    public ResponseData<?> sortAndSpecificationSearch(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "id:desc", required = false) String sortBy,
            @RequestParam(defaultValue = "id!=0", required = false) String search) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Specification Search",
                albumService.sortAndSpecificationSearch(pageNo, pageSize, sortBy, search));
    }
}