package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.gen.apis.AccountsApi;
import jp.co.goalist.gsc.gen.dtos.LoginInfoDto;
import jp.co.goalist.gsc.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccountController implements AccountsApi {

    private final AccountService accountService;

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<LoginInfoDto> proxyLogin(String accountId, String accountType) {
        log.info("proxyLogin, accountId: {}", accountId);
        return ResponseEntity.ok(accountService.proxyLogin(accountId, accountType));
    }
}
