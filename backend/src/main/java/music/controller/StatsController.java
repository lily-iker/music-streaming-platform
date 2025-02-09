package music.controller;

import lombok.RequiredArgsConstructor;
import music.dto.response.ApiResponse;
import music.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getStats() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get Stats",
                statsService.getStats());
    }
}
