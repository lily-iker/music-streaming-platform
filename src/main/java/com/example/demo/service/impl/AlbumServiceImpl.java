package com.example.demo.service.impl;

import com.example.demo.dto.request.AlbumRequestForArtist;
import com.example.demo.dto.response.AlbumResponse;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.SearchAlbumResponse;
import com.example.demo.exception.ImageUploadException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Album;
import com.example.demo.model.Artist;
import com.example.demo.model.Song;
import com.example.demo.repository.AlbumRepository;
import com.example.demo.repository.ArtistRepository;
import com.example.demo.repository.search.AlbumSearchRepository;
import com.example.demo.repository.specification.AlbumSpecificationBuilder;
import com.example.demo.repository.specification.SearchOperator;
import com.example.demo.service.AlbumService;
import com.example.demo.service.CloudinaryService;
import com.example.demo.utils.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final CloudinaryService cloudinaryService;
    private final AlbumSearchRepository albumSearchRepository;

    @Override
    @Transactional
    public long addAlbum(String artistName, AlbumRequestForArtist albumRequest, MultipartFile imageFile) {
        Artist artist = artistRepository.findByName(artistName)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        Album album = new Album();

        if (imageFile == null || imageFile.isEmpty())
            throw new ImageUploadException("Failed to upload image");

        try {
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            album.setName(albumRequest.getName());
            album.setImageUrl(imageUrl);
        }
        catch (IOException e) {
            throw new ImageUploadException("Failed to upload image due to an I/O error ", e.getCause());
        }

        artist.saveAlbum(album);

        albumRepository.save(album);

        return album.getId();
    }

    @Override
    public AlbumResponse getAlbum(Long id) {
        Album album = albumRepository.findByIdWithAllFields(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

        Long artistId = album.getArtist().getId();
        String artistName = album.getArtist().getName();

        List<Long> songIds = album.getSongs().stream()
                .map(Song::getId)
                .toList();
        List<String> songNames = album.getSongs().stream()
                .map(Song::getName)
                .toList();

        return AlbumResponse.builder()
                .name(album.getName())
                .imageUrl(album.getImageUrl())
                .artistId(artistId)
                .artistName(artistName)
                .songIds(songIds)
                .songNames(songNames)
                .build();
    }

    @Override
    @Transactional
    public long updateAlbum(Long id, AlbumRequestForArtist albumRequest, MultipartFile imageFile) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

        if (imageFile == null || imageFile.isEmpty())
            throw new ImageUploadException("Failed to upload image");

        try {
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            album.setName(albumRequest.getName());
            album.setImageUrl(imageUrl);
        }
        catch (IOException e) {
            throw new ImageUploadException("Failed to upload image due to an I/O error ", e.getCause());
        }

        return id;
    }

    @Override
    public long deleteAlbum(Long id) {
        if (albumRepository.findById(id).isPresent())
            albumRepository.deleteById(id);
        return id;
    }

    @Override
    public PageResponse<?> getAllAlbums(int pageNo, int pageSize, String sortBy) {
        Sort sort = SortUtil.resolveSortBy(sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = albumRepository.findAllIds(pageable);

        List<Album> albums = albumRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchAlbumResponse> searchAlbumResponses = albumsToSearchAlbumResponses(albums);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(ids.getTotalPages())
                .items(searchAlbumResponses)
                .build();
    }

    @Override
    public PageResponse<?> getAlbumsByName(int pageNo, int pageSize, String sortBy, String name) {
        Sort sort = SortUtil.resolveSortBy(sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = albumRepository.findAllIdsByName(name, pageable);

        List<Album> albums = albumRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchAlbumResponse> searchAlbumResponses = albumsToSearchAlbumResponses(albums);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(ids.getTotalPages())
                .items(searchAlbumResponses)
                .build();
    }

    @Override
    public PageResponse<?> sortAndSpecificationSearch(int pageNo, int pageSize, String sortBy, String search) {
        AlbumSpecificationBuilder builder = new AlbumSpecificationBuilder();

        if (StringUtils.hasLength(search)) {
            Pattern pattern = Pattern.compile("([,']?)(\\w+)(!:|!~|!=|>=|<=|[:~=><])(\\w+)");
            Matcher matcher = pattern.matcher(search);

            while (matcher.find()) {
                String andOrLogic = matcher.group(1);
                String key = matcher.group(2);
                String operator = matcher.group(3);
                String value = matcher.group(4);

                if (andOrLogic != null && (andOrLogic.equals(SearchOperator.AND_OPERATOR) ||
                        andOrLogic.equals(SearchOperator.OR_OPERATOR))) {
                    builder.with(andOrLogic, key, operator, value, null, null);
                } else {
                    builder.with(key, operator, value, null, null);
                }
            }
        }

        Sort sort = SortUtil.resolveSortBy(sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = albumSearchRepository.findIdsBySpecification(builder.build(), pageable);

        List<Album> albums = albumRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchAlbumResponse> searchAlbumResponses = albumsToSearchAlbumResponses(albums);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(ids.getTotalPages())
                .items(searchAlbumResponses)
                .build();
    }

    private List<SearchAlbumResponse> albumsToSearchAlbumResponses(List<Album> albums) {
        return albums.stream()
                .map(album -> {
                    Long artistId = album.getArtist().getId();
                    String artistName = album.getArtist().getName();

                    return SearchAlbumResponse.builder()
                            .id(album.getId())
                            .name(album.getName())
                            .imageUrl(album.getImageUrl())
                            .artistId(artistId)
                            .artistName(artistName)
                            .build();
                })
                .toList();
    }
}
