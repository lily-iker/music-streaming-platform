package com.example.demo.controller;

import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.request.RegisterRequestForArtist;
import com.example.demo.dto.request.SignInRequest;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseData<TokenResponse> login(@Valid @RequestBody SignInRequest request) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Login success",
                authenticationService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseData<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Register success",
                authenticationService.register(request));
    }

    @PostMapping("/register-for-artist")
    public ResponseData<TokenResponse> registerForArtist(@Valid @RequestBody RegisterRequestForArtist request) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Register success",
                authenticationService.registerForArtist(request));
    }

    @PostMapping("/logout")
    public ResponseData<String> logout(@NonNull HttpServletRequest request) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Logout success",
                authenticationService.logout(request));
    }

    @PostMapping("/refresh")
    public ResponseData<TokenResponse> refresh(@NonNull HttpServletRequest request) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Refresh token success",
                authenticationService.refresh(request));
    }
}

