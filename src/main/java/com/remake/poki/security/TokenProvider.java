package com.remake.poki.security;

import com.remake.poki.model.User;
import com.remake.poki.model.Version;
import com.remake.poki.repository.VersionRepository;
import com.remake.poki.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private final VersionRepository versionRepository;

    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpirationInMs;

    public String generateTokenWithUser(User user, String version) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("username", user.getUser())
                .claim("version", version)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.BEARER)) {
            return bearerToken.substring(7);
        }
        String jwt = request.getParameter(Constants.AUTHORIZATION);
        if (StringUtils.hasText(jwt)) {
            return jwt;
        }
        return null;
    }

    public String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.BEARER)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);

            // Kiểm tra version của token
            String tokenVersion = claims.get("version", String.class);
            String currentVersion = getCurrentVersion();
            if (tokenVersion == null || !tokenVersion.equals(currentVersion)) {
                log.error("Invalid token version. Token version: {}, Current version: {}", tokenVersion, currentVersion);
                return false;
            }
            return true;
        } catch (SecurityException ex) {
            log.warn("JWT invalid signature");
        } catch (MalformedJwtException ex) {
            log.warn("JWT malformed");
        } catch (ExpiredJwtException ex) {
            log.info("JWT expired");
        } catch (UnsupportedJwtException ex) {
            log.warn("JWT unsupported");
        } catch (IllegalArgumentException ex) {
            log.warn("JWT claims empty");
        }
        return false;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("username").toString();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String getCurrentVersion() {
        return versionRepository.findByIsActiveTrue().map(Version::getVersion).orElse("1.0.0"); // Default version nếu chưa có trong DB
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
