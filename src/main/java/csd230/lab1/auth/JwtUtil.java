package csd230.lab1.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class that creates, reads and checks JWTs.
 * A token has three parts separated by dots: header.payload.signature.
 */
@Component
public class JwtUtil {

    // Demo only: a hardcoded secret is fine for learning, but never do this in production.
    // The same secret signs the token when it is created and checks it when it comes back.
    private final String secret_key = "mysecretkey";

    // How long a token is valid, in milliseconds: 60 * 60 * 1000 = 1 hour
    private long accessTokenValidity = 60 * 60 * 1000;

    private final JwtParser jwtParser;

    // The token travels in the HTTP header "Authorization: Bearer <token>"
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil() {
        // The parser verifies the signature using the secret
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
    }

    // Builds a signed token with the username and the roles (for example ROLE_ADMIN) inside
    public String createToken(String username, Collection<? extends GrantedAuthority> authorities) {
        // "subject" (sub) is the standard claim used for the username
        Claims claims = Jwts.claims().setSubject(username);

        // Turn the authorities into a simple list of texts so they can be saved in the token
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenValidity);

        // HS256 signs the content. The signature detects changes, but it does not hide the content
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    // Reads the "Authorization" header and removes the "Bearer " prefix.
    // Returns null when the request has no token.
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    // Reads the claims (the data inside the token). It throws an exception if the signature
    // is wrong or the token is expired.
    public Claims resolveClaims(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token != null) {
            return jwtParser.parseClaimsJws(token).getBody();
        }
        return null;
    }

    // Extra check: the expiration date must still be in the future
    public boolean validateClaims(Claims claims) {
        return claims.getExpiration().after(new Date());
    }
}
