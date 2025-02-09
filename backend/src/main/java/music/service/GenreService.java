package music.service;

import music.dto.response.SearchGenreResponse;

import java.util.List;

public interface GenreService {
    List<SearchGenreResponse> getAllGenres();
}
