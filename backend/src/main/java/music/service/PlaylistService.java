package music.service;

import music.model.Playlist;

import java.util.Set;

public interface PlaylistService {
    Long createMyPlaylist(String name);
    Playlist getPlaylistById(Long id);
    Set<Playlist> getAllMyPlaylists();
    Long updateMyPlaylistName(Long id, String name);
    Long deleteMyPlaylist(Long id);
    Long addSongToMyPlaylist(Long playlistId, Long songId);
    Long removeSongFromMyPlaylist(Long playlistId, Long songId);
}
