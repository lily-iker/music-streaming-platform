package com.example.demo.service.impl;

import com.example.demo.constant.GenreName;
import com.example.demo.dto.request.*;
import com.example.demo.dto.response.ArtistResponse;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.SearchArtistResponse;
import com.example.demo.dto.response.SongResponse;
import com.example.demo.exception.*;
import com.example.demo.mapper.ArtistMapper;
import com.example.demo.mapper.SongMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.repository.search.ArtistSearchRepository;
import com.example.demo.repository.specification.ArtistSpecificationBuilder;
import com.example.demo.repository.specification.SearchOperator;
import com.example.demo.service.ArtistService;
import com.example.demo.service.CloudinaryService;
import com.example.demo.service.SongService;
import com.example.demo.utils.SortUtil;
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
import java.util.stream.Collectors;

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
    private final SongService songService;
    private final ArtistSearchRepository artistSearchRepository;

    @Value("${CLOUDINARY_MAX_IMAGE_SIZE}")
    private int maxImageSize;

    @Value("${CLOUDINARY_MAX_AUDIO_SIZE}")
    private int maxAudioSize;

    @Override
    @Transactional
    public long addArtist(String artistName, UserRequest userRequest) {
        if (artistRepository.findByName(artistName).isPresent())
            throw new DataInUseException("Artist name is in use");

        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        userRequest.getRoles().forEach(roleRequest -> {
            Role role = roleRepository.findByNameWithUsers(roleRequest.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            roles.add(role);
        });

        roles.forEach(user::saveRole);

        userRepository.save(user);

        Artist artist = new Artist();
        artist.setName(artistName);

        artist.saveUser(user);

        artistRepository.save(artist);

        return artist.getId();
    }

    @Override
    public ArtistResponse getArtist(Long id) {
        Artist artist = artistRepository.findWithSongsAndAlbums(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        List<Long> songIds = artist.getSongs().stream()
                .map(Song::getId)
                .toList();
        List<String> songNames = artist.getSongs().stream()
                .map(Song::getName)
                .toList();
        List<Long> albumIds = artist.getAlbums().stream()
                .map(Album::getId)
                .toList();
        List<String> albumNames = artist.getAlbums().stream()
                .map(Album::getName)
                .toList();

        ArtistResponse artistResponse = artistMapper.toArtistResponse(artist);
        artistResponse.setSongIds(songIds);
        artistResponse.setSongNames(songNames);
        artistResponse.setAlbumIds(albumIds);
        artistResponse.setAlbumNames(albumNames);

        return artistResponse;
    }

    @Override
    @Transactional
    public long updateArtist(Long id, UpdateArtistInfoRequest request, MultipartFile imageFile) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        isImageValid(imageFile);

        try {
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            artist.setBio(request.getBio());
            artist.setImageUrl(imageUrl);
        }
        catch (IOException e) {
            throw new ImageUploadException("Failed to upload image due to an I/O error ", e.getCause());
        }

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
    public long addMySong(SongRequestForArtist songRequest,
                          MultipartFile imageFile, MultipartFile songFile) throws IOException{
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

        isImageValid(imageFile);
        isAudioValid(songFile);

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

        Set<GenreName> genreNames = songRequest.getGenres().stream()
                .map(GenreRequest::getName)
                .collect(Collectors.toSet());

        Set<Genre> genres = genreRepository.findByNameIn(genreNames);
        if (genres.size() != genreNames.size()) {
            throw new ResourceNotFoundException("One or more genres were not found");
        }

        Set<String> collaborateArtistNames = songRequest.getCollaborateArtists().stream()
                .map(ArtistRequest::getName)
                .collect(Collectors.toSet());

        Set<Artist> collaborateArtists = artistRepository.findByNameIn(collaborateArtistNames);
        if (collaborateArtists.size() != collaborateArtistNames.size()) {
            throw new ResourceNotFoundException("One or more artists were not found");
        }

        Album album = albumRepository.findByName(songRequest.getAlbum().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

        genres.forEach(song::saveGenre);
        collaborateArtists.forEach(song::saveArtist);
        song.saveAlbum(album);

        artist.saveSong(song);

        songRepository.save(song);

        return song.getId();
    }

    @Override
    @Transactional
    public long addMyAlbum(AlbumRequestForArtist albumRequest, MultipartFile imageFile) {
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

        isImageValid(imageFile);

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

        song.saveAlbum(album);

        return songId;
    }

    @Override
    @Transactional
    public long updateMySong(Long songId, SongRequestForArtist songRequest,
                             MultipartFile imageFile, MultipartFile songFile) throws IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        if (artist == null)
            throw new InvalidDataException("There seems to be an issue with your artist profile. Please contact support");

        SongResponse songResponse = songService.getSong(songId);

        if (!songResponse.getArtistIds().contains(artist.getId())
                || !songResponse.getArtistNames().contains(artist.getName())) {
            throw new AccessDenyException("You are not one of the artists for this song");
        }

        isImageValid(imageFile);
        isAudioValid(songFile);

        String imageUrl = cloudinaryService.uploadImage(imageFile);
        if (imageUrl == null) {
            throw new ImageUploadException("Failed to upload image");
        }

        String songUrl = cloudinaryService.uploadAudio(songFile);
        if (songUrl == null) {
            throw new AudioUploadException("Failed to upload audio");
        }

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));
        if (!artist.getSongs().contains(song))
            throw new AccessDenyException("You are not the artist of this song");

        songMapper.updateSong(song, songRequest);
        song.setImageUrl(imageUrl);
        song.setSongUrl(songUrl);

        Set<GenreName> genreNames = songRequest.getGenres().stream()
                .map(GenreRequest::getName)
                .collect(Collectors.toSet());

        Set<Genre> genres = genreRepository.findByNameIn(genreNames);
        if (genres.size() != genreNames.size()) {
            throw new ResourceNotFoundException("One or more genres were not found");
        }

        Set<String> collaborateArtistNames = songRequest.getCollaborateArtists().stream()
                .map(ArtistRequest::getName)
                .collect(Collectors.toSet());

        Set<Artist> collaborateArtists = artistRepository.findByNameIn(collaborateArtistNames);
        if (collaborateArtists.size() != collaborateArtistNames.size()) {
            throw new ResourceNotFoundException("One or more artists were not found");
        }

        Album album = albumRepository.findByName(songRequest.getAlbum().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

        genres.forEach(song::saveGenre);
        collaborateArtists.forEach(song::saveArtist);
        song.saveAlbum(album);

        artist.saveSong(song);

        return song.getId();
    }

    @Override
    @Transactional
    public long updateMyAlbum(Long albumId, AlbumRequestForArtist albumRequest, MultipartFile imageFile) {
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

        isImageValid(imageFile);

        try {
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            album.setName(albumRequest.getName());
            album.setImageUrl(imageUrl);
        }
        catch (IOException e) {
            throw new ImageUploadException("Failed to upload image due to an I/O error ", e.getCause());
        }

        return album.getId();
    }

    @Override
    @Transactional
    public long updateMyArtistPage(UpdateArtistInfoRequest request, MultipartFile imageFile) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        if (artist == null)
            throw new InvalidDataException("There seems to be an issue with your artist profile. Please contact support");

        isImageValid(imageFile);

        try {
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            artist.setBio(request.getBio());
            artist.setImageUrl(imageUrl);
        }
        catch (IOException e) {
            throw new ImageUploadException("Failed to upload image due to an I/O error ", e.getCause());
        }

        return artist.getId();
    }

    @Override
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
        Sort sort = SortUtil.resolveSortBy(sortBy, "followers");

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = artistRepository.findAllIds(pageable);

        List<Artist> artists = artistRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchArtistResponse> artistResponses = artistsToArtistResponses(artists);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(ids.getTotalPages())
                .items(artistResponses)
                .build();
    }

    @Override
    public PageResponse<?> getArtistsByName(int pageNo, int pageSize, String sortBy, String name) {
        Sort sort = SortUtil.resolveSortBy(sortBy, "followers");

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = artistRepository.findAllIdsByName(name, pageable);

        List<Artist> artists = artistRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchArtistResponse> artistResponses = artistsToArtistResponses(artists);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(ids.getTotalPages())
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

        Sort sort = SortUtil.resolveSortBy(sortBy, "followers");

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Long> ids = artistSearchRepository.findIdsBySpecification(builder.build(), pageable);

        List<Artist> artists = artistRepository.findAllByIdsAndSort(ids.getContent(), sort);

        List<SearchArtistResponse> artistResponses = artistsToArtistResponses(artists);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(ids.getTotalPages())
                .items(artistResponses)
                .build();
    }

    private List<SearchArtistResponse> artistsToArtistResponses(List<Artist> artists) {
        return artists.stream()
                .map(artistMapper::toSearchArtistResponse)
                .toList();
    }

    private void isImageValid(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty())
            throw new ImageUploadException("Failed to upload image");
        if (imageFile.getSize() > maxImageSize)
            throw new ImageUploadException("Image file size exceeds the maximum limit of " +
                    (maxImageSize / (1024 * 1024)) + " MB");
    }

    private void isAudioValid(MultipartFile songFile) {
        if (songFile == null || songFile.isEmpty())
            throw new AudioUploadException("Failed to upload audio");
        if (songFile.getSize() > maxAudioSize)
            throw new AudioUploadException("Audio file size exceeds the maximum limit of " +
                    (maxAudioSize / (1024 * 1024)) + " MB");
    }
}
