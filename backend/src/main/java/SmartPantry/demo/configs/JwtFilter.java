package SmartPantry.demo.configs;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${security.enabled}")
    private boolean securityEnabled;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            if (securityEnabled) {
                String path = httpRequest.getRequestURI();
                
                // Permit pre-flight OPTIONS requests
                if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
                    chain.doFilter(request, response);
                    return;
                }

                if (path.startsWith("/api/v1/auth") || path.equals("/api/v1/health") ||
                    path.startsWith("/swagger") || path.startsWith("/v3/api-docs") ||
                    path.equals("/swagger-ui.html") || path.equals("/")) {
                    chain.doFilter(request, response);
                    return;
                }

                String authHeader = httpRequest.getHeader("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
                    return;
                }

                String token = authHeader.substring(7);
                if (!jwtUtil.validateToken(token)) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                    return;
                }

                Long userId = jwtUtil.extractId(token);
                String username = jwtUtil.extractName(token);
                
                // Set custom UserContext
                UserContext.setCurrentUserId(userId);
                UserContext.setCurrentUsername(username);

                // IMPORTANT: Set Spring Security context so authorizeHttpRequests passes
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
            SecurityContextHolder.clearContext();
        }
    }
}