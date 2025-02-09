package music.service;

import music.dto.request.RegisterRequest;
import music.dto.request.RegisterRequestForArtist;
import music.dto.request.SignInRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import music.dto.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse authenticate(SignInRequest request, HttpServletResponse response);
    TokenResponse register(RegisterRequest request, HttpServletResponse response);
    TokenResponse registerForArtist(RegisterRequestForArtist request, HttpServletResponse response);
    TokenResponse refresh(HttpServletRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
}
