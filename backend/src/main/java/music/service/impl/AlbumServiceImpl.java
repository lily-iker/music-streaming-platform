package music.service.impl;

import lombok.RequiredArgsConstructor;
import music.dto.request.AlbumRequest;
import music.dto.request.UpdateAlbumRequest;
import music.dto.response.AlbumResponse;
import music.dto.response.PageResponse;
import music.dto.response.SearchAlbumResponse;
import music.dto.response.SongResponse;
import music.exception.ImageUploadException;
import music.exception.ResourceNotFoundException;
import music.model.Album;
import music.model.Artist;
import music.repository.AlbumRepository;
import music.repository.ArtistRepository;
import music.repository.search.AlbumSearchRepository;
import music.repository.specification.AlbumSpecificationBuilder;
import music.repository.specification.SearchOperator;
import music.service.AlbumService;
import music.service.CloudinaryService;
import music.utils.MultipartFileUtil;
import music.utils.SortUtil;
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
    private final SortUtil sortUtil;
    private final MultipartFileUtil multipartFileUtil;

    @Override
    @Transactional
    public long addAlbum(AlbumRequest albumRequest,
                         MultipartFile imageFile) throws IOException {
        Artist artist = artistRepository.findByName(albumRequest.getArtistName())
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        Album album = new Album();

        multipartFileUtil.isImageValid(imageFile);
        String imageUrl = cloudinaryService.uploadImage(imageFile);
        if (imageUrl == null) {
            throw new ImageUploadException("Failed to upload image");
        }

        album.setName(albumRequest.getName());
        album.setImageUrl(imageUrl);
        album.setArtist(artist);

        albumRepository.save(album);

        return album.getId();
    }

    @Override
    public AlbumResponse getAlbum(Long id) {
        Album album = albumRepository.findByIdWithAllFields(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

        Long artistId = album.getArtist().getId();
        String artistName = album.getArtist().getName();

        List<SongResponse> songs = album.getSongs().stream()
                .map(song -> SongResponse.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .imageUrl(song.getImageUrl())
                        .songUrl(song.getSongUrl())
                        .likeCount(song.getLikeCount())
                        .duration(song.getDuration())
                        .artistIds(List.of(artistId))
                        .artistNames(List.of(artistName))
                        .build())
                .toList();

        return AlbumResponse.builder()
                .name(album.getName())
                .imageUrl(album.getImageUrl())
                .artistId(artistId)
                .artistName(artistName)
                .songs(songs)
                .build();
    }

    @Override
    @Transactional
    public long updateAlbum(Long id,
                            UpdateAlbumRequest updateAlbumRequest,
                            MultipartFile imageFile) throws IOException {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

        album.setName(updateAlbumRequest.getName());
        if (imageFile != null) {
            multipartFileUtil.isImageValid(imageFile);
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            if (imageUrl == null) {
                throw new ImageUploadException("Failed to upload image");
            }

            album.setImageUrl(imageUrl);
        }

        return id;
    }

    @Override
    @Transactional
    public long deleteAlbum(Long id) {
        albumRepository.deleteById(id);
        return id;
    }

    @Override
    public PageResponse<?> getAllAlbums(int pageNo, int pageSize, String sortBy) {
        Sort sort = sortUtil.resolveSortBy(sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = albumRepository.findAllIds(pageable);

        List<Album> albums = albumRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchAlbumResponse> searchAlbumResponses = albumsToSearchAlbumResponses(albums);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(ids.getTotalPages())
                .items(searchAlbumResponses)
                .build();
    }

    @Override
    public PageResponse<?> getAlbumsByName(int pageNo, int pageSize, String sortBy, String name) {
        Sort sort = sortUtil.resolveSortBy(sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = albumRepository.findAllIdsByName(name, pageable);

        List<Album> albums = albumRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchAlbumResponse> searchAlbumResponses = albumsToSearchAlbumResponses(albums);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(ids.getTotalPages())
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

        Sort sort = sortUtil.resolveSortBy(sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = albumSearchRepository.findIdsBySpecification(builder.build(), pageable);

        List<Album> albums = albumRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchAlbumResponse> searchAlbumResponses = albumsToSearchAlbumResponses(albums);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(ids.getTotalPages())
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
