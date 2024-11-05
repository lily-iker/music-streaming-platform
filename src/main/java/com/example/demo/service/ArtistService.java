package com.example.demo.service;

import com.example.demo.dto.request.AlbumRequestForArtist;
import com.example.demo.dto.request.SongRequestForArtist;
import com.example.demo.dto.request.UpdateArtistInfoRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.ArtistResponse;
import com.example.demo.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ArtistService {
    long addArtist(String artistName, UserRequest userRequest);
    ArtistResponse getArtist(Long id);
    long updateArtist(Long id, UpdateArtistInfoRequest request, MultipartFile imageFile);
    long deleteArtist(Long id);
    long addMySong(SongRequestForArtist songRequest, MultipartFile imageFile, MultipartFile songFile) throws IOException;
    long addMyAlbum(AlbumRequestForArtist albumRequest, MultipartFile imageFile);
    long addMySongToMyAlbum(Long songId, Long albumId);
    long updateMySong(Long songId, SongRequestForArtist songRequest, MultipartFile imageFile, MultipartFile songFile) throws IOException;
    long updateMyAlbum(Long albumId, AlbumRequestForArtist albumRequest, MultipartFile imageFile);
    long updateMyArtistPage(UpdateArtistInfoRequest request, MultipartFile imageFile);
    long deleteMySong(Long songId);
    long deleteMyAlbum(Long albumId);
    long deleteMySongFromMyAlbum(Long songId, Long albumId);
    PageResponse<?> getAllArtists(int pageNo, int pageSize, String sortBy);
    PageResponse<?> getArtistsByName(int pageNo, int pageSize, String sortBy, String name);
    PageResponse<?> sortAndSpecificationSearch(int pageNo, int pageSize, String sortBy, String search);
}
