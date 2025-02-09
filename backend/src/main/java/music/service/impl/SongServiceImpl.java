package music.service.impl;

import lombok.RequiredArgsConstructor;
import music.constant.GenreName;
import music.dto.request.SongRequest;
import music.dto.request.UpdateSongRequest;
import music.dto.response.PageResponse;
import music.dto.response.SearchSongResponse;
import music.dto.response.SongResponse;
import music.exception.AudioUploadException;
import music.exception.ImageUploadException;
import music.exception.InvalidDataException;
import music.exception.ResourceNotFoundException;
import music.mapper.SongMapper;
import music.model.Album;
import music.model.Artist;
import music.model.Genre;
import music.model.Song;
import music.repository.AlbumRepository;
import music.repository.ArtistRepository;
import music.repository.GenreRepository;
import music.repository.SongRepository;
import music.repository.search.SongSearchRepository;
import music.repository.specification.SearchOperator;
import music.repository.specification.SongSpecificationBuilder;
import music.service.CloudinaryService;
import music.service.SongService;
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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final GenreRepository genreRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final SongSearchRepository songSearchRepository;
    private final CloudinaryService cloudinaryService;
    private final SortUtil sortUtil;
    private final MultipartFileUtil multipartFileUtil;

    @Override
    @Transactional
    public long addSong(SongRequest request,
                        MultipartFile imageFile,
                        MultipartFile songFile) throws IOException {
        if (request.getGenreNames().isEmpty() || request.getArtistNames().isEmpty()) {
            throw new InvalidDataException("Genres and artists must not be empty");
        }

        Song song = songMapper.toSong(request);

        Set<String> genreNames = request.getGenreNames();

        Set<Genre> genres = genreRepository.findByNameIn(genreNames);
        if (genres.size() != genreNames.size()) {
            throw new ResourceNotFoundException("One or more genres were not found");
        }

        Set<String> artistNames = request.getArtistNames();

        Set<Artist> artists = artistRepository.findByNameIn(artistNames);
        if (artists.size() != artistNames.size()) {
            throw new ResourceNotFoundException("One or more artists were not found");
        }
        Album album = null;
        if (!request.getAlbumName().isEmpty() && !request.getAlbumName().equals("no-album")) {
            album = albumRepository.findByName(request.getAlbumName())
                    .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

            if (artists.size() != 1) {
                throw new InvalidDataException("Album can has only 1 artist");
            }
            if (!artists.iterator().next().getId()
                    .equals(album.getArtist().getId())) {
                throw new InvalidDataException("This artist is not the album owner");
            }
        }

        multipartFileUtil.isImageValid(imageFile);
        multipartFileUtil.isAudioValid(songFile);

        String imageUrl = cloudinaryService.uploadImage(imageFile);
        if (imageUrl == null) {
            throw new ImageUploadException("Failed to upload image");
        }

        String songUrl = cloudinaryService.uploadAudio(songFile);
        if (songUrl == null) {
            throw new AudioUploadException("Failed to upload audio");
        }

        song.setImageUrl(imageUrl);
        song.setSongUrl(songUrl);

        genres.forEach(genre -> genre.getSongs().add(song));
        song.setArtists(artists);
        if (album != null)
            song.setAlbum(album);

        songRepository.save(song);

        return song.getId();
    }

    @Override
    public SongResponse getSong(Long id) {
        Song song = songRepository.findByIdWithAllFields(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        // Song -> SongResponse
        List<Integer> genreIds = song.getGenres().stream()
                .map(Genre::getId)
                .toList();
        List<GenreName> genreNames = song.getGenres().stream()
                .map(Genre::getName)
                .toList();
        List<Long> artistIds = song.getArtists().stream()
                .map(Artist::getId)
                .toList();
        List<String> artistNames = song.getArtists().stream()
                .map(Artist::getName)
                .toList();
        List<String> artistImageUrls = song.getArtists().stream()
                .map(Artist::getImageUrl)
                .toList();

        SongResponse songResponse = songMapper.toSongResponse(song);
        songResponse.setGenreIds(genreIds);
        songResponse.setGenreNames(genreNames);
        songResponse.setArtistIds(artistIds);
        songResponse.setArtistNames(artistNames);
        songResponse.setArtistImageUrls(artistImageUrls);

        Album album = song.getAlbum();
        if (album != null) {
            songResponse.setAlbumId(album.getId());
            songResponse.setAlbumName(album.getName());
            songResponse.setAlbumImageUrl(album.getImageUrl());
        }

        return songResponse;
    }

    @Override
    @Transactional
    public long updateSong(Long id,
                           UpdateSongRequest request,
                           MultipartFile imageFile,
                           MultipartFile songFile) throws IOException {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));

        songMapper.updateSong(song, request);

        // Delete song from genres
        song.getGenres().forEach(genre -> genre.getSongs().remove(song));

        Set<String> genreNames = request.getGenreNames();

        Set<Genre> genres = genreRepository.findByNameIn(request.getGenreNames());
        if (genres.size() != genreNames.size()) {
            throw new ResourceNotFoundException("One or more genres were not found");
        }

        if (imageFile != null) {
            multipartFileUtil.isImageValid(imageFile);
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            if (imageUrl == null) {
                throw new ImageUploadException("Failed to upload image");
            }

            song.setImageUrl(imageUrl);
        }

        if (songFile != null) {
            multipartFileUtil.isAudioValid(songFile);
            String songUrl = cloudinaryService.uploadAudio(songFile);
            if (songUrl == null) {
                throw new AudioUploadException("Failed to upload audio");
            }

            song.setSongUrl(songUrl);
        }

        genres.forEach(genre -> genre.getSongs().add(song));

        return song.getId();
    }

    @Override
    @Transactional
    public long deleteSong(Long id) {
        if (songRepository.existsById(id))
            songRepository.deleteById(id);
        return id;
    }

    @Override
    public PageResponse<?> getAllSongs(int pageNo, int pageSize, String sortBy) {
        Sort sort = sortUtil.resolveSortBy(sortBy, "likeCount");

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = songRepository.findAllIds(pageable);

        List<Song> songs = songRepository.findAllByIdsAndSort(ids.toList(), sort);

        List<SearchSongResponse> searchSongResponses = songsToSearchSongResponses(songs);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(ids.getTotalPages())
                .items(searchSongResponses)
                .build();
    }

    @Override
    public PageResponse<?> getSongsByName(int pageNo, int pageSize, String sortBy, String name) {
        Sort sort = sortUtil.resolveSortBy(sortBy, "likeCount");

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = songRepository.findAllIdsByName(name, pageable);

        List<Song> songs = songRepository.findAllByIdsAndSort(ids.toList(), sort);

        List<SearchSongResponse> searchSongResponses = songsToSearchSongResponses(songs);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(ids.getTotalPages())
                .items(searchSongResponses)
                .build();
    }

    @Override
    public PageResponse<?> getSongsByGenre(int pageNo, int pageSize, String sortBy, Integer genreId) {
        Sort sort = sortUtil.resolveSortBy(sortBy, "likeCount");

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = songRepository.findAllIdsByGenre(genreId, pageable);

        List<Song> songs = songRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchSongResponse> searchSongResponses = songsToSearchSongResponses(songs);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(ids.getTotalPages())
                .items(searchSongResponses)
                .build();
    }

    @Override
    public PageResponse<?> sortAndSpecificationSearch(int pageNo, int pageSize, String sortBy, String search) {

        SongSpecificationBuilder builder = new SongSpecificationBuilder();

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

        Sort sort = sortUtil.resolveSortBy(sortBy, "likeCount");

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = songSearchRepository.findIdsBySpecification(builder.build(), pageable);

        List<Song> songs = songRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchSongResponse> searchSongResponses = songsToSearchSongResponses(songs);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(ids.getTotalPages())
                .items(searchSongResponses)
                .build();
    }

    @Override
    public List<?> getFeaturedSongs() {
        List<Long> songIds = songRepository.find6RandomSongIds();
        return songsToSearchSongResponses(songRepository.findSongsWithArtistsByIds(songIds));
    }

    @Override
    public List<?> getMadeForYouSongs() {
        List<Long> songIds = List.of(1L, 2L, 3L, 4L, 5L, 6L);
        return songsToSearchSongResponses(songRepository.findSongsWithArtistsByIds(songIds));
    }

    @Override
    public List<?> getTrendingSongs() {
        List<Long> songIds = songRepository.find6MostLikedSongIds();
        return songsToSearchSongResponses(songRepository.findSongsWithArtistsByIds(songIds));
    }

    // List<Song> -> List<SearchSongResponse>
    private List<SearchSongResponse> songsToSearchSongResponses(List<Song> songs) {
        return songs.stream()
                .map(song -> {
                    List<Long> artistIds = song.getArtists().stream()
                            .map(Artist::getId)
                            .toList();
                    List<String> artistNames = song.getArtists().stream()
                            .map(Artist::getName)
                            .toList();

                    SearchSongResponse searchSongResponse = songMapper.toSearchSongResponse(song);
                    searchSongResponse.setArtistIds(artistIds);
                    searchSongResponse.setArtistNames(artistNames);

                    return searchSongResponse;
                })
                .toList();
    }
}
