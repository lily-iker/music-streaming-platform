package music.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.accessExpiryTime}")
    private int accessExpiryTime;

    @Value("${jwt.refreshExpiryTime}")
    private int refreshExpiryTime;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(Map.of("scope", buildScope(userDetails.getAuthorities())), userDetails);
    }

    public String generateAccessToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setIssuer("https://github.com/lily-iker")
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiryTime))
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setIssuer("https://github.com/lily-iker")
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiryTime))
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(String token, TokenType tokenType, UserDetails userDetails) {
        String username = extractUsername(token, tokenType);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token, tokenType);
    }

    public String extractUsername(String token, TokenType tokenType) {
        return extractClaims(token, tokenType, Claims::getSubject);
    }

    public Date extractExpired(String token, TokenType tokenType) {
        return extractClaims(token, tokenType, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token, TokenType tokenType) {
        return extractExpired(token, tokenType).before(new Date());
    }

    private Key getKey(TokenType tokenType) {
        if (TokenType.ACCESS_TOKEN.equals(tokenType)){
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        }
        else{
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
        }
    }

    private <T> T extractClaims(String token, TokenType tokenType, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey(tokenType))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claimsResolver.apply(claims);
    }

    private String buildScope(Collection <? extends GrantedAuthority> authorities) {
        StringJoiner result = new StringJoiner(" ");
        authorities.forEach(authority -> result.add(authority.toString()));
        return result.toString();
    }
}
