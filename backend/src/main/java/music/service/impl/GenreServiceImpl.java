package music.service.impl;

import music.dto.response.SearchGenreResponse;
import music.model.Genre;
import music.repository.GenreRepository;
import music.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<SearchGenreResponse> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();

        return genres.stream()
                .map(genre -> SearchGenreResponse.builder()
                        .id(genre.getId())
                        .name(genre.getName())
                        .build())
                .toList();
    }


}
