package jp.co.goalist.gsc.middlewares;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.services.AuthService;
import jp.co.goalist.gsc.services.JwtService;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        // empty header
        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);

        if (StringUtils.isNotEmpty(email) &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            Account currentUser;
            try {
                currentUser = authService.loadUserByUsername(email);
            } catch (UsernameNotFoundException ex) {
                log.error("User not found: {}", email);
                handleUnauthorizedException(response);
                return;
            }
            if (jwtService.isTokenValid(jwt, currentUser)) {
                GeneralUtils.setToRequestAttribute("jwt", jwt);
                GeneralUtils.setToRequestAttribute("permissions", jwtService.getPermissions(jwt));
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        currentUser,
                        currentUser.getPassword(),
                        currentUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleUnauthorizedException(HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(ErrorMessage.UNAUTHORIZED.getStatusCode())
                .message(ErrorMessage.UNAUTHORIZED.getMessage())
                .build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().write(GeneralUtils.toJson(errorResponse).getBytes(StandardCharsets.UTF_8));
        response.flushBuffer();
    }
}
