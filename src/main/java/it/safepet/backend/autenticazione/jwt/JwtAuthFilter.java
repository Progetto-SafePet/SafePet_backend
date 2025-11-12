package it.safepet.backend.autenticazione.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Filtro che valida il token JWT e popola il contesto utente.
 */
public class JwtAuthFilter extends HttpFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Evita di bloccare le richieste CORS preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
            return;
        }

        // Estrai header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        // Verifica token
        if (!jwtUtil.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return;
        }

        // Estrai info utente
        Long userId = jwtUtil.getId(token);
        String email = jwtUtil.getEmail(token);
        Role role = jwtUtil.getRole(token);

        AuthContext.setCurrentUser(new AuthenticatedUser(userId, email, role));

        try {
            chain.doFilter(request, response);
        } finally {
            AuthContext.clear(); // sempre pulizia dopo la richiesta
        }
    }
}
