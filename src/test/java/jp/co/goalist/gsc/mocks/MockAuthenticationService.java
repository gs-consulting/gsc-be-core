package jp.co.goalist.gsc.mocks;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class MockAuthenticationService {

    public void mockAuthenticatedAccount(String username, String role) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                username,
                "password",
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    public void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }
}
