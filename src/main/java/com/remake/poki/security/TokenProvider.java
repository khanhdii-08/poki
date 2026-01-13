package com.remake.poki.security;

import com.remake.poki.model.Version;
import com.remake.poki.repository.VersionRepository;
import com.remake.poki.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.SecretKey;
import java.security.Principal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private final VersionRepository versionRepository;

    public String resolveToken(ServerHttpRequest request) {
        List<String> auths = request.getHeaders().get(Constants.AUTHORIZATION);
        String bearerToken = (auths != null && !auths.isEmpty()) ? auths.get(0) : null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.BEARER)) {
            return bearerToken.substring(7);
        }
        MultiValueMap<String, String> params = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        String jwt = params.getFirst(Constants.TOKEN);
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
            String currentVersion = getCurrentTokenVersion();

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

    public Authentication getAuthentication(String token) {
        /*
         * Currently not use
         * */
//        Claims claims = getClaims(token);
//        Collection<? extends GrantedAuthority> authorities = Arrays
//                .stream(claims.get(Constants.AUTHORIZATION).toString().split(","))
//                .filter(auth -> !auth.trim().isEmpty())
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//
//        User principal = new User(claims.getSubject(), "", authorities);
        Principal principal = () -> String.valueOf(getUserIdFromToken(token));
        return new UsernamePasswordAuthenticationToken(principal, null, null);
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String getCurrentTokenVersion() {
        return versionRepository.findFirstByOrderByIdDesc().map(Version::getVersion).orElse("1.0.0"); // Default version nếu chưa có trong DB
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
