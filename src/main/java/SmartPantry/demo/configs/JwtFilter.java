package SmartPantry.demo.configs;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
                UserContext.setCurrentUserId(userId);
            }

            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}