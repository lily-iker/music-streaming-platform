package music.service.impl;

import music.dto.request.*;
import music.dto.response.ArtistResponse;
import music.dto.response.PageResponse;
import music.dto.response.SearchArtistResponse;
import music.dto.response.SongResponse;
import music.exception.*;
import music.mapper.ArtistMapper;
import music.mapper.SongMapper;
import music.mapper.UserMapper;
import music.model.*;
import music.repository.*;
import music.repository.search.ArtistSearchRepository;
import music.repository.specification.ArtistSpecificationBuilder;
import music.repository.specification.SearchOperator;
import music.service.ArtistService;
import music.service.CloudinaryService;
import music.utils.MultipartFileUtil;
import music.utils.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ArtistMapper artistMapper;
    private final UserMapper userMapper;
    private final CloudinaryService cloudinaryService;
    private final SongMapper songMapper;
    private final GenreRepository genreRepository;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final ArtistSearchRepository artistSearchRepository;
    private final SortUtil sortUtil;
    private final MultipartFileUtil multipartFileUtil;

    @Value("${CLOUDINARY_MAX_IMAGE_SIZE}")
    private int maxImageSize;

    @Value("${CLOUDINARY_MAX_AUDIO_SIZE}")
    private int maxAudioSize;

    @Override
    @Transactional
    public long addArtist(String artistName,
                          UserRequest userRequest,
                          MultipartFile imageFile) throws IOException {
        if (artistRepository.findByName(artistName).isPresent())
            throw new DataInUseException("Artist name is in use");

        multipartFileUtil.isImageValid(imageFile);
        String imageUrl = cloudinaryService.uploadImage(imageFile);

        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        userRequest.getRoles().forEach(roleRequest -> {
            Role role = roleRepository.findByNameWithUsers(roleRequest.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            roles.add(role);
        });

        roles.forEach(role -> role.getUsers().add(user));

        userRepository.save(user);

        Artist artist = new Artist();
        artist.setName(artistName);
        artist.setImageUrl(imageUrl);

        artist.setUser(user);

        artistRepository.save(artist);

        return artist.getId();
    }

    @Override
    public ArtistResponse getArtist(Long id) {
        Artist artist = artistRepository.findWithAlbums(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        List<Long> albumIds = artist.getAlbums().stream()
                .map(Album::getId)
                .toList();
        List<String> albumNames = artist.getAlbums().stream()
                .map(Album::getName)
                .toList();
        List<String> albumImageUrls = artist.getAlbums().stream()
                .map(Album::getImageUrl)
                .toList();

        ArtistResponse artistResponse = artistMapper.toArtistResponse(artist);

        List<Song> songsByArtist = songRepository.findAllByArtistId(id);
        List<SongResponse> songResponses = songsByArtist.stream()
                .map(song -> {
                    List<Long> artistIds = song.getArtists().stream()
                            .map(Artist::getId)
                            .toList();
                    List<String> artistNames = song.getArtists().stream()
                            .map(Artist::getName)
                            .toList();

                    return SongResponse.builder()
                            .id(song.getId())
                            .name(song.getName())
                            .imageUrl(song.getImageUrl())
                            .songUrl(song.getSongUrl())
                            .likeCount(song.getLikeCount())
                            .duration(song.getDuration())
                            .artistIds(artistIds)
                            .artistNames(artistNames)
                            .build();
                })
                .toList();

        artistResponse.setSongs(songResponses);
        artistResponse.setAlbumIds(albumIds);
        artistResponse.setAlbumNames(albumNames);
        artistResponse.setAlbumImageUrls(albumImageUrls);

        return artistResponse;
    }

    @Override
    @Transactional
    public long updateArtist(Long id,
                             UpdateArtistInfoRequest request,
                             MultipartFile imageFile) throws IOException {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        multipartFileUtil.isImageValid(imageFile);
        String imageUrl = cloudinaryService.uploadImage(imageFile);

        artist.setBio(request.getBio());
        artist.setImageUrl(imageUrl);

        return id;
    }

    @Override
    public long deleteArtist(Long id) {
        if (artistRepository.existsById(id))
            artistRepository.deleteById(id);
        return id;
    }

    @Override
    @Transactional
    public long addMySong(SongRequest songRequest,
                          MultipartFile imageFile,
                          MultipartFile songFile) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        if (artist == null)
            throw new InvalidDataException("There seems to be an issue with your artist profile. Please contact support");

        boolean songExisted = artist.getSongs().stream()
                .anyMatch(song -> song.getName().equals(songRequest.getName()));

        if (songExisted)
            throw new DataInUseException("You already have a song with that name");

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

        Song song = songMapper.toSong(songRequest);
        song.setImageUrl(imageUrl);
        song.setSongUrl(songUrl);

        Set<String> genreNames = songRequest.getGenreNames();

        Set<Genre> genres = genreRepository.findByNameIn(genreNames);
        if (genres.size() != genreNames.size()) {
            throw new ResourceNotFoundException("One or more genres were not found");
        }

        Set<String> collaborateArtistNames = songRequest.getArtistNames();

        Set<Artist> collaborateArtists = artistRepository.findByNameIn(collaborateArtistNames);
        if (collaborateArtists.size() != collaborateArtistNames.size()) {
            throw new ResourceNotFoundException("One or more artists were not found");
        }

        Album album = albumRepository.findByName(songRequest.getAlbumName())
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

        genres.forEach(genre -> genre.getSongs().add(song));
        collaborateArtists.add(artist);
        song.setArtists(collaborateArtists);
        song.setAlbum(album);

        songRepository.save(song);

        return song.getId();
    }

    @Override
    @Transactional
    public long addMyAlbum(AlbumRequest albumRequest, MultipartFile imageFile) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        if (artist == null)
            throw new InvalidDataException("There seems to be an issue with your artist profile. Please contact support");

        boolean albumExisted = artist.getAlbums().stream()
                .anyMatch(album -> album.getName().equals(albumRequest.getName()));

        if (albumExisted)
            throw new DataInUseException("You already have a album with that name");

        Album album = new Album();

        multipartFileUtil.isImageValid(imageFile);
        String imageUrl = cloudinaryService.uploadImage(imageFile);

        album.setName(albumRequest.getName());
        album.setImageUrl(imageUrl);

        album.setArtist(artist);

        albumRepository.save(album);

        return album.getId();
    }

    @Override
    @Transactional
    public long addMySongToMyAlbum(Long songId, Long albumId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        if (artist == null)
            throw new InvalidDataException("There seems to be an issue with your artist profile. Please contact support");

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        if (!artist.getSongs().contains(song))
            throw new AccessDenyException("You are not the artist of this song");
        if (song.getArtists().size() != 1 || !song.getArtists().contains(artist))
            throw new AccessDenyException("A song can only be added to an album if it has exactly one artist.");

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

        if (!artist.getAlbums().contains(album))
            throw new AccessDenyException("You are not the artist of this album");

        song.setAlbum(album);

        return songId;
    }

    @Override
    @Transactional
    public long updateMySong(Long songId,
                             UpdateSongRequest songRequest,
                             MultipartFile imageFile,
                             MultipartFile songFile) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        if (artist == null)
            throw new InvalidDataException("There seems to be an issue with your artist profile. Please contact support");

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));
        if (!artist.getSongs().contains(song))
            throw new AccessDenyException("You are not the artist of this song");
        if (!song.getArtists().contains(artist)) {
            throw new AccessDenyException("You are not one of the artists for this song");
        }

        songMapper.updateSong(song, songRequest);

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

        Set<String> genreNames = songRequest.getGenreNames();

        Set<Genre> genres = genreRepository.findByNameIn(genreNames);
        if (genres.size() != genreNames.size()) {
            throw new ResourceNotFoundException("One or more genres were not found");
        }

        genres.forEach(genre -> genre.getSongs().add(song));

        return song.getId();
    }

    @Override
    @Transactional
    public long updateMyAlbum(Long albumId,
                              AlbumRequest albumRequest,
                              MultipartFile imageFile) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        if (artist == null)
            throw new InvalidDataException("There seems to be an issue with your artist profile. Please contact support");

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

        if (!artist.getAlbums().contains(album))
            throw new AccessDenyException("You are not the artist of this album");

        multipartFileUtil.isImageValid(imageFile);

        String imageUrl = cloudinaryService.uploadImage(imageFile);
        album.setName(albumRequest.getName());
        album.setImageUrl(imageUrl);

        return album.getId();
    }

    @Override
    @Transactional
    public long updateMyArtistPage(UpdateArtistInfoRequest request, MultipartFile imageFile) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        if (artist == null)
            throw new InvalidDataException("There seems to be an issue with your artist profile. Please contact support");

        multipartFileUtil.isImageValid(imageFile);

        String imageUrl = cloudinaryService.uploadImage(imageFile);
        artist.setBio(request.getBio());
        artist.setImageUrl(imageUrl);

        return artist.getId();
    }

    @Override
    @Transactional
    public long deleteMySong(Long songId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        if (artist == null)
            throw new InvalidDataException("There seems to be an issue with your artist profile. Please contact support");

        songRepository.deleteById(songId);

        return songId;
    }

    @Override
    @Transactional
    public long deleteMyAlbum(Long albumId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        if (artist == null)
            throw new InvalidDataException("There seems to be an issue with your artist profile. Please contact support");

        albumRepository.deleteById(albumId);

        return albumId;
    }

    @Override
    @Transactional
    public long deleteMySongFromMyAlbum(Long songId, Long albumId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        if (artist == null)
            throw new InvalidDataException("There seems to be an issue with your artist profile. Please contact support");

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        if (!artist.getSongs().contains(song))
            throw new AccessDenyException("You are not the artist of this song");
        if (song.getArtists().size() != 1 || !song.getArtists().contains(artist))
            throw new AccessDenyException("A song can only be added to an album if it has exactly one artist.");


        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

        if (!artist.getAlbums().contains(album))
            throw new AccessDenyException("You are not the artist of this album");

        song.setAlbum(null);
        album.setSongs(new HashSet<>());

        return songId;
    }

    @Override
    public PageResponse<?> getAllArtists(int pageNo, int pageSize, String sortBy) {
        Sort sort = sortUtil.resolveSortBy(sortBy, "followers");

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = artistRepository.findAllIds(pageable);

        List<Artist> artists = artistRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchArtistResponse> artistResponses = artistsToArtistResponses(artists);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(ids.getTotalPages())
                .items(artistResponses)
                .build();
    }

    @Override
    public PageResponse<?> getArtistsByName(int pageNo, int pageSize, String sortBy, String name) {
        Sort sort = sortUtil.resolveSortBy(sortBy, "followers");

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = artistRepository.findAllIdsByName(name, pageable);

        List<Artist> artists = artistRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchArtistResponse> artistResponses = artistsToArtistResponses(artists);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(ids.getTotalPages())
                .items(artistResponses)
                .build();
    }

    @Override
    public PageResponse<?> sortAndSpecificationSearch(int pageNo, int pageSize, String sortBy, String search) {
        ArtistSpecificationBuilder builder = new ArtistSpecificationBuilder();

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

        Sort sort = sortUtil.resolveSortBy(sortBy, "followers");

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = artistSearchRepository.findIdsBySpecification(builder.build(), pageable);

        List<Artist> artists = artistRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchArtistResponse> artistResponses = artistsToArtistResponses(artists);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(ids.getTotalPages())
                .items(artistResponses)
                .build();
    }

    @Override
    public List<?> getTopArtists() {
        List<Long> artistIds = artistRepository.find6MostFollowedArtistIds();
        return artistsToArtistResponses(artistRepository.findAllById(artistIds));
    }

    private List<SearchArtistResponse> artistsToArtistResponses(List<Artist> artists) {
        return artists.stream()
                .map(artistMapper::toSearchArtistResponse)
                .toList();
    }

}
