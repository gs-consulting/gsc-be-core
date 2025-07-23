package jp.co.goalist.gsc.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final AccountRepository accountRepository;

    /**
     * Load user by email
     * 
     * @param username login by email address
     * @return Account
     */
    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> opAccount = accountRepository.findByEmail(username);
        if (opAccount.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return opAccount.get();
    }
}
