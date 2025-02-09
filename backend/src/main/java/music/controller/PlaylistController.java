package music.controller;

import music.dto.response.ApiResponse;
import music.model.Playlist;
import music.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/playlist")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;

    @PostMapping("")
    public ApiResponse<Long> createMyPlaylist(@RequestParam String name) {
        return new ApiResponse<>(HttpStatus.CREATED.value(),
                "Create playlist success",
                playlistService.createMyPlaylist(name));
    }

    @GetMapping("/{playlistId}")
    public ApiResponse<Playlist> getPlaylistById(@PathVariable Long playlistId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get playlist success",
                playlistService.getPlaylistById(playlistId));
    }

    @GetMapping("/all")
    public ApiResponse<Set<Playlist>> getAllMyPlaylists() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get all playlists success",
                playlistService.getAllMyPlaylists());
    }

    @PutMapping("/{playlistId}")
    public ApiResponse<Long> updateMyPlaylistName(@PathVariable Long playlistId,
                                                  @RequestParam String name) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Update playlist name success",
                playlistService.updateMyPlaylistName(playlistId, name));
    }

    @DeleteMapping("/{playlistId}")
    public ApiResponse<Long> deleteMyPlaylist(@PathVariable Long playlistId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Delete playlist success",
                playlistService.deleteMyPlaylist(playlistId));
    }

    @PostMapping("/{playlistId}/song/{songId}")
    public ApiResponse<Long> addSongToMyPlaylist(@PathVariable Long playlistId,
                                                 @PathVariable Long songId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Add song to playlist success",
                playlistService.addSongToMyPlaylist(playlistId, songId));
    }

    @DeleteMapping("/{playlistId}/song/{songId}")
    public ApiResponse<Long> removeSongFromMyPlaylist(@PathVariable Long playlistId,
                                                      @PathVariable Long songId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Remove song from playlist success",
                playlistService.removeSongFromMyPlaylist(playlistId, songId));
    }
}

