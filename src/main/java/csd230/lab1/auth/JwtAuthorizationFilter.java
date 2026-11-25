package csd230.lab1.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Filter that runs before the controllers. If the request has a valid Bearer token,
 * it tells Spring Security who the user is. OncePerRequestFilter makes sure it runs
 * only one time for each request.
 */
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Look for a token in the Authorization header
        String accessToken = jwtUtil.resolveToken(request);

        // 2. No token: do nothing here. The security rules decide if the request is allowed
        //    (a protected API request without any login gets 401).
        if (accessToken != null) {
            try {
                // 3. Check the signature and the expiration date
                Claims claims = jwtUtil.resolveClaims(request);
                if (claims != null && jwtUtil.validateClaims(claims)) {
                    String username = claims.getSubject();

                    // 4. Turn the roles saved in the token back into Spring Security authorities.
                    //    The "ROLE_" prefix is kept, so hasRole("ADMIN") keeps working.
                    List<String> roles = (List<String>) claims.get("roles");
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    if (roles != null) {
                        for (String role : roles) {
                            authorities.add(new SimpleGrantedAuthority(role));
                        }
                    }

                    // 5. Tell Spring Security who is making this request
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(username, "", authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // 6. Invalid or expired token: answer 401 with a small JSON message and stop here
                String details = e.getMessage() == null ? "" : e.getMessage();
                // Escape backslashes and quotes so the JSON text stays valid
                details = details.replace("\\", "\\\\").replace("\"", "\\\"");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(
                        "{\"message\":\"Authentication Error\",\"details\":\"" + details + "\"}");
                return;
            }
        }

        // 7. Continue with the next filter (and finally the controller)
        filterChain.doFilter(request, response);
    }
}
