package music.controller;

import music.service.GenreService;
import music.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/genre")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/all")
    public ApiResponse<?> getAllGenres() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get all genres success",
                genreService.getAllGenres());
    }
}
