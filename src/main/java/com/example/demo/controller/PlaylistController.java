package com.example.demo.controller;

import com.example.demo.dto.response.ResponseData;
import com.example.demo.model.Playlist;
import com.example.demo.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;

    @PostMapping("")
    public ResponseData<Long> createMyPlaylist(@RequestParam String name) {
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Create playlist success",
                playlistService.createMyPlaylist(name));
    }

    @GetMapping("/{playlistId}")
    public ResponseData<Playlist> getPlaylistById(@PathVariable Long playlistId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get playlist success",
                playlistService.getPlaylistById(playlistId));
    }

    @GetMapping("/all")
    public ResponseData<Set<Playlist>> getAllMyPlaylists() {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get all playlists success",
                playlistService.getAllMyPlaylists());
    }

    @PutMapping("/{playlistId}")
    public ResponseData<Long> updateMyPlaylistName(@PathVariable Long playlistId,
                                                   @RequestParam String name) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Update playlist name success",
                playlistService.updateMyPlaylistName(playlistId, name));
    }

    @DeleteMapping("/{playlistId}")
    public ResponseData<Long> deleteMyPlaylist(@PathVariable Long playlistId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Delete playlist success",
                playlistService.deleteMyPlaylist(playlistId));
    }

    @PostMapping("/{playlistId}/song/{songId}")
    public ResponseData<Long> addSongToMyPlaylist(@PathVariable Long playlistId,
                                                  @PathVariable Long songId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Add song to playlist success",
                playlistService.addSongToMyPlaylist(playlistId, songId));
    }

    @DeleteMapping("/{playlistId}/song/{songId}")
    public ResponseData<Long> removeSongFromMyPlaylist(@PathVariable Long playlistId,
                                                       @PathVariable Long songId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Remove song from playlist success",
                playlistService.removeSongFromMyPlaylist(playlistId, songId));
    }
}

