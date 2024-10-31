package com.example.demo.service.impl;

import com.example.demo.constant.RoleName;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.request.RegisterRequestForArtist;
import com.example.demo.dto.request.SignInRequest;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.exception.DataInUseException;
import com.example.demo.exception.InvalidDataException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Artist;
import com.example.demo.model.Role;
import com.example.demo.model.Token;
import com.example.demo.model.User;
import com.example.demo.repository.ArtistRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.TokenService;
import com.example.demo.utils.JwtUtil;
import com.example.demo.utils.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ArtistRepository artistRepository;

    @Override
    public TokenResponse authenticate(SignInRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword()));

        var user = userRepository.findByUsernameWithRoles(request.getUsername())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + request.getUsername()));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        tokenService.saveToken(Token.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .username(request.getUsername())
                .build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    @Transactional
    public TokenResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DataInUseException("Username is already in use");
        }

        Role userRole = roleRepository.findByNameWithUsers(RoleName.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role 'USER' not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        user.saveRole(userRole);

        userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    @Transactional
    public TokenResponse registerForArtist(RegisterRequestForArtist request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DataInUseException("Username is already in use");
        }

        Set<RoleName> roleNames = new HashSet<>();
        roleNames.add(RoleName.USER);
        roleNames.add(RoleName.ARTIST);

        Set<Role> roles = roleRepository.findByNameIn(roleNames);
        if (roles.size() != roleNames.size()) {
            throw new ResourceNotFoundException("One or more roles were not found");
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        roles.forEach(user::saveRole);

        Artist artist = Artist.builder()
                .name(request.getName())
                .build();

        artist.setUser(user);

        userRepository.save(user);
        artistRepository.save(artist);

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh");
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token can not be blank");
        }

        final String username = jwtUtil.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);

        User user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (jwtUtil.isValidToken(refreshToken, TokenType.REFRESH_TOKEN, user)) {
            throw new InvalidDataException("Invalid Token");
        }

        String accessToken = jwtUtil.generateAccessToken(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public String logout(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh");
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token can not be blank");
        }

        final String username = jwtUtil.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        tokenService.deleteToken(username);
        return "Logout success";
    }

}

