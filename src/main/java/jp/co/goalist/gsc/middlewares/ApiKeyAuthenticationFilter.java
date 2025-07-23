package jp.co.goalist.gsc.middlewares;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final String expectedApiKey;

    public ApiKeyAuthenticationFilter(String expectedApiKey) {
        this.expectedApiKey = expectedApiKey;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String apiKey = request.getHeader("API-KEY");

        if (expectedApiKey.equals(apiKey)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("crawler",
                    null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
