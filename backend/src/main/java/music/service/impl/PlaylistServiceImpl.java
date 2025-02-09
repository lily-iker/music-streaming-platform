package music.service.impl;

import music.exception.DataInUseException;
import music.exception.ResourceNotFoundException;
import music.model.Playlist;
import music.model.Song;
import music.model.User;
import music.repository.PlaylistRepository;
import music.repository.SongRepository;
import music.repository.UserRepository;
import music.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    @Override
    @Transactional
    public Long createMyPlaylist(String name) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        User user = userRepository.findByUsernameWithPlaylists(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<Playlist> existedPlaylists = user.getPlaylists();

        existedPlaylists.forEach(existedPlaylist -> {
            if (existedPlaylist.getName().equals(name))
                throw new DataInUseException("You already have a playlist name: " + name);
        });

        Playlist playlist = Playlist.builder()
                .name(name)
                .songs(new HashSet<>())
                .build();

        savePlaylistToUser(user, playlist);

        playlistRepository.save(playlist);

        return playlist.getId();
    }

    @Override
    public Playlist getPlaylistById(Long id) {
        return playlistRepository.findByIdWithAllFields(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));
    }

    @Override
    public Set<Playlist> getAllMyPlaylists() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        User user = userRepository.findByUsernameWithPlaylists(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getPlaylists();
    }

    @Override
    @Transactional
    public Long updateMyPlaylistName(Long id, String name) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        User user = userRepository.findByUsernameWithPlaylists(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<Playlist> existedPlaylists = user.getPlaylists();

        Playlist playlistToUpdate = existedPlaylists.stream()
                .filter(existedPlaylist -> existedPlaylist.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        existedPlaylists.forEach(existedPlaylist -> {
            if (existedPlaylist.getName().equals(name) &&
                !existedPlaylist.getId().equals(id))
                throw new DataInUseException("You already have a playlist name: " + name);
        });

        playlistToUpdate.setName(name);

        return playlistToUpdate.getId();
    }

    @Override
    @Transactional
    public Long deleteMyPlaylist(Long id) {
        playlistRepository.deleteById(id);
        return id;
    }

    @Override
    @Transactional
    public Long addSongToMyPlaylist(Long playlistId, Long songId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        User user = userRepository.findByUsernameWithPlaylistsAndSongs(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Playlist playlist = user.getPlaylists().stream()
                .filter(existedPlaylist -> existedPlaylist.getId().equals(playlistId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

//        or
//        Playlist playlist = playlistRepository.findById(playlistId)
//                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        playlist.getSongs().add(song);

        return playlistId;
    }

    @Override
    @Transactional
    public Long removeSongFromMyPlaylist(Long playlistId, Long songId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();

        User user = userRepository.findByUsernameWithPlaylistsAndSongs(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Playlist playlist = user.getPlaylists().stream()
                .filter(existedPlaylist -> existedPlaylist.getId().equals(playlistId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

//        or
//        Playlist playlist = playlistRepository.findById(playlistId)
//                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        playlist.getSongs().remove(song);

        return playlistId;
    }

    private void savePlaylistToUser(User user, Playlist playlist) {
        if (playlist != null) {
            if (user.getPlaylists() == null) {
                user.setPlaylists(new HashSet<>());
            }
            user.getPlaylists().add(playlist);
            playlist.setUser(user);
        }
    }

}
