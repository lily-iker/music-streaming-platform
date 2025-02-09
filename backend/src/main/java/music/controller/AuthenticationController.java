package music.controller;

import music.dto.request.RegisterRequest;
import music.dto.request.RegisterRequestForArtist;
import music.dto.request.SignInRequest;
import music.dto.response.ApiResponse;
import music.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody SignInRequest request,
                                @NonNull HttpServletResponse response) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Login success",
                authenticationService.authenticate(request, response));
    }

    @PostMapping("/register")
    public ApiResponse<?> register(@Valid @RequestBody RegisterRequest request,
                                   @NonNull HttpServletResponse response) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Register success",
                authenticationService.register(request, response));
    }

    @PostMapping("/register-for-artist")
    public ApiResponse<?> registerForArtist(@Valid @RequestBody RegisterRequestForArtist request,
                                            @NonNull HttpServletResponse response) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Register success",
                authenticationService.registerForArtist(request, response));
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@NonNull HttpServletRequest request,
                                      @NonNull HttpServletResponse response) {
        authenticationService.logout(request, response);
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Logout success");
    }

    @PostMapping("/refresh")
    public ApiResponse<?> refresh(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Refresh token success",
                authenticationService.refresh(request, response));
    }
}

