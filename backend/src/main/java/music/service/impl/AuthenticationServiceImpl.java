package music.service.impl;

import music.constant.RoleName;
import music.dto.request.RegisterRequest;
import music.dto.request.RegisterRequestForArtist;
import music.dto.request.SignInRequest;
import music.dto.response.TokenResponse;
import music.exception.DataInUseException;
import music.exception.InvalidDataException;
import music.exception.ResourceNotFoundException;
import music.mapper.UserMapper;
import music.model.Artist;
import music.model.Role;
import music.model.User;
import music.repository.ArtistRepository;
import music.repository.RoleRepository;
import music.repository.UserRepository;
import music.service.AuthenticationService;
import music.utils.JwtUtil;
import music.utils.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ArtistRepository artistRepository;

    @Override
    public TokenResponse authenticate(SignInRequest request, HttpServletResponse response) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword()));

        var user = userRepository.findByUsernameWithRoles(request.getUsername())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + request.getUsername()));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setAttribute("SameSite", "Strict");
        accessTokenCookie.setMaxAge(60 * 60);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 14);

        // Add the cookies to the response
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    @Transactional
    public TokenResponse register(RegisterRequest request, HttpServletResponse response) {
        if (!isValidUsername(request.getUsername())) {
            throw new InvalidDataException("Username should not contain 'admin'");
        }

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
                .active(true)
                .roles(roles)
                .build();

        roles.forEach(role -> role.getUsers().add(user));

        userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setAttribute("SameSite", "Strict");
        accessTokenCookie.setMaxAge(60 * 60);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 14);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    @Transactional
    public TokenResponse registerForArtist(RegisterRequestForArtist request, HttpServletResponse response) {
        if (!isValidUsername(request.getUsername())) {
            throw new InvalidDataException("Username should not contain 'admin'");
        }

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
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        roles.forEach(role -> role.getUsers().add(user));

        Artist artist = Artist.builder()
                .name(request.getName())
                .build();

        artist.setUser(user);

        userRepository.save(user);
        artistRepository.save(artist);

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setAttribute("SameSite", "Strict");
        accessTokenCookie.setMaxAge(60 * 60);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 14);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            refreshToken = Arrays.stream(cookies)
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse("");
        }

        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token can not be blank");
        }

        final String username = jwtUtil.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);

        User user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (!jwtUtil.isValidToken(refreshToken, TokenType.REFRESH_TOKEN, user)) {
            throw new InvalidDataException("Invalid Token");
        }

        String accessToken = jwtUtil.generateAccessToken(user);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setAttribute("SameSite", "Strict");
        accessTokenCookie.setMaxAge(60 * 60);

        response.addCookie(accessTokenCookie);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshCookie = new Cookie("refreshToken", "");
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");
        refreshCookie.setSecure(true);
        refreshCookie.setHttpOnly(true);

        Cookie accessCookie = new Cookie("accessToken", "");
        accessCookie.setMaxAge(0);
        accessCookie.setPath("/");
        accessCookie.setSecure(true);

        response.addCookie(refreshCookie);
        response.addCookie(accessCookie);
    }

    private boolean isValidUsername(String username) {
        String regex = "(?i).*admin.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        return !matcher.matches();
    }

}

