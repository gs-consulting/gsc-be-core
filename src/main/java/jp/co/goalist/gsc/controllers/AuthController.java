package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.gen.dtos.ResetPasswordFormDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jp.co.goalist.gsc.gen.apis.AuthApi;
import jp.co.goalist.gsc.gen.dtos.LoginFormDto;
import jp.co.goalist.gsc.gen.dtos.LoginInfoDto;
import jp.co.goalist.gsc.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AccountService accountService;

    public ResponseEntity<LoginInfoDto> login(LoginFormDto loginFormDto) {
        log.info("login, account {}", loginFormDto.getEmail());
        return ResponseEntity.ok(accountService.login(loginFormDto));
    }

    @Override
    public ResponseEntity<Void> resetPassword(ResetPasswordFormDto resetPasswordFormDto) {
        accountService.resetPassword(resetPasswordFormDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
