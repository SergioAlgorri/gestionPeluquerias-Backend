package gestionPeluqueria.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    public JwtUtil jwtUtil;
    @Autowired
    public CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String token = null;

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring("Bearer".length()).trim();
                email = jwtUtil.extractUserEmail(token);
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
                if (jwtUtil.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                            SecurityConstants.MESSAGE_UNAUTHORIZED);
                    return;
                }
            }

            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, SecurityConstants.MESSAGE_UNAUTHORIZED);
        }
    }
}