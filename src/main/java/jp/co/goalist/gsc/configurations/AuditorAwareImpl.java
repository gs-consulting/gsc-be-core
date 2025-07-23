package jp.co.goalist.gsc.configurations;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jp.co.goalist.gsc.entities.Account;

public class AuditorAwareImpl implements AuditorAware<String> {

    @SuppressWarnings("null")
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return Optional.of("anonymous");
        }

        Account account = (Account) auth.getPrincipal();
        return Optional.of(account.getId());
    }
    
    
}
