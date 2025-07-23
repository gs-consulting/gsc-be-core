package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.Role;
import jp.co.goalist.gsc.enums.ScreenPermission;
import jp.co.goalist.gsc.enums.SubRole;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.enums.TokenType;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.*;
import jp.co.goalist.gsc.utils.GeneralUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final OemAccountRepository oemAccountRepository;
    private final OperatorAccountRepository operatorAccountRepo;
    private final UtilService utilService;

    public Account getExistingAccountByEmail(String email) {
        Optional<Account> optionalOne = accountRepository.findByEmail(email);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.ACCOUNT.getTargetName(),
                            email))
                    .fieldError("email")
                    .build());
        }

        return optionalOne.get();
    }

    @Transactional
    public LoginInfoDto login(LoginFormDto loginFormDto) {
        // use authenticationManager of Spring to authenticate
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginFormDto.getEmail(),
                loginFormDto.getPassword()));

        Account loggedAccount = (Account) auth.getPrincipal();
        if (!loggedAccount.getIsNormalLogin()) {
            throw new UsernameNotFoundException("User not found");
        }

        LoginInfoDto loginInfoDto = LoginInfoDto.builder()
                .email(loggedAccount.getEmail())
                .fullName(loggedAccount.getFullName())
                .role(loggedAccount.getRole())
                .build();

        if (loggedAccount.getRole().equals(Role.OPERATOR.getId())) {
            String jwt = jwtService.generateToken(loggedAccount, null, false, null);
            loginInfoDto.setAccessToken(jwt);
        }        
        else if (loggedAccount.getRole().equals(Role.OEM.getId())) {
            String jwt = jwtService.generateToken(loggedAccount, null, false, null);
            loginInfoDto.setAccessToken(jwt);

            Optional<OemAccount> oemAccount = oemAccountRepository.findById(loggedAccount.getId());
            if (oemAccount.isEmpty()) {
                throw new NotFoundException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                        .message(String.format(
                                ErrorMessage.NOT_FOUND.getMessage(),
                                TargetName.ACCOUNT.getTargetName(),
                                loggedAccount.getEmail()))
                        .fieldError("email")
                        .build());
            }

            loginInfoDto.setOemGroupName(oemAccount.get().getOemGroup().getOemGroupName());
        } else if (loggedAccount.getRole().equals(Role.CLIENT.getId())) {
            if (loggedAccount.getSubRole().equals(SubRole.OPERATOR.getId())) {
                OperatorClientAccount operatorClientAccount = utilService.getExistingOperatorClientAccountById(loggedAccount.getId());
                List<String> permissions = operatorClientAccount.getPermissions();
                OperatorClientAccount parent = operatorClientAccount.getParent();
                if (Objects.nonNull(parent) && !parent.getIsDomainEnabled()) {
                    permissions.remove(ScreenPermission.MESSAGE.getId());
                }
                loginInfoDto.setPermissions(permissions);

                String jwt = jwtService.generateToken(loggedAccount, null, false, permissions);
                loginInfoDto.setAccessToken(jwt);
            } else {
                OemClientAccount oemClientAccount = utilService.getExistingOemClientAccountById(loggedAccount.getId());
                List<String> permissions = oemClientAccount.getPermissions();
                OemClientAccount parent = oemClientAccount.getParent();
                if (Objects.nonNull(parent) && !parent.getIsDomainEnabled()) {
                    permissions.remove(ScreenPermission.MESSAGE.getId());
                }
                loginInfoDto.setPermissions(permissions);

                String jwt = jwtService.generateToken(loggedAccount, null, false, permissions);
                loginInfoDto.setAccessToken(jwt);
            }
        }
        return loginInfoDto;
    }

    public void resetPassword(ResetPasswordFormDto resetPasswordFormDto) {
        String token = GeneralUtils.decodeString(resetPasswordFormDto.getToken());
        String[] split = token.split("&");

        if (split.length != 4) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_TOKEN.getStatusCode())
                    .message(ErrorMessage.INVALID_TOKEN.getMessage())
                    .build());
        }

        Account account = getExistingAccountByEmail(split[2]);
        if (!Objects.equals(account.getPassword(), split[0])) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.EXPIRED_TOKEN.getStatusCode())
                    .message(ErrorMessage.EXPIRED_TOKEN.getMessage())
                    .build());
        }

        if (Objects.equals(TokenType.REGISTER.name(), split[3]) && !Objects.equals(account.getResetTokenString(), split[1])) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.EXPIRED_TOKEN.getStatusCode())
                    .message(ErrorMessage.EXPIRED_TOKEN.getMessage())
                    .build());
        }

        if (Objects.equals(TokenType.FORGOT.name(), split[3]) && !Objects.equals(account.getForgotTokenString(), split[1])) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.EXPIRED_TOKEN.getStatusCode())
                    .message(ErrorMessage.EXPIRED_TOKEN.getMessage())
                    .build());
        }

        if (account.getTokenExpirationDate().isBefore(LocalDateTime.now())) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.EXPIRED_TOKEN.getStatusCode())
                    .message(ErrorMessage.EXPIRED_TOKEN.getMessage())
                    .build());
        }

        if (!Objects.equals(resetPasswordFormDto.getPassword(), resetPasswordFormDto.getConfirmPassword())) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.MISMATCH_PWD.getStatusCode())
                    .message(ErrorMessage.MISMATCH_PWD.getMessage())
                    .build());
        }

        account.setPassword(passwordEncoder.encode(resetPasswordFormDto.getPassword()));
        account.setTokenExpirationDate(null);
        if (Objects.equals(TokenType.FORGOT.name(), split[3])) {
            account.setForgotTokenString(null);
        } else {
            account.setResetTokenString(null);
        }
        account.setEnabled(true);
        accountRepository.saveAndFlush(account);
    }

    public void createOperatorAccount() {
        String operatorEmail = "gsc.admin@goalist.co.jp";
        Optional<Account> existing = accountRepository.findByEmail(operatorEmail);
        if (existing.isPresent()) {
            return;
        }

        Account account = new Account();
        account.setEmail(operatorEmail);
        account.setFullName("GSC Admin");
        account.setRole(Role.OPERATOR.getId());
        account.setPassword(passwordEncoder.encode("Goalist@3988"));
        account.setIsDeleted(false);
        account.setEnabled(true);
        account.setCreatedBy("System");
        account.setUpdatedBy("System");
        account = accountRepository.saveAndFlush(account);

        OperatorAccount operatorAccount = new OperatorAccount();
        operatorAccount.setId(account.getId());
        operatorAccount.setFullName(account.getFullName());
        operatorAccount.setCreatedBy("System");
        operatorAccount.setUpdatedBy("System");
        operatorAccountRepo.saveAndFlush(operatorAccount);
    }

    public void checkDuplicateEmail(String email) {
        Optional<Account> existingAccount = accountRepository.findByEmail(email);
        if (existingAccount.isPresent()) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.DUPLICATE_DATA.getStatusCode())
                    .message(String.format(ErrorMessage.DUPLICATE_DATA.getMessage(),
                            TargetName.ACCOUNT.getTargetName(),
                            email))
                    .build());
        }
    }

    @Transactional
    public Account createNewAccount(String email, String fullName, String password, String token, Role role, Role subRole) {
        Account newAccount = Account.builder()
                .email(email)
                .fullName(fullName)
                .password(password)
                .resetTokenString(token)
                .tokenExpirationDate(LocalDateTime.now().plusHours(24))
                .role(role.getId())
                .subRole(Objects.nonNull(subRole) ? subRole.getId() : null)
                .build();
        return accountRepository.saveAndFlush(newAccount);
    }

    @Transactional
    public Account createNewClientAccount(String email, String fullName, String password, SubRole subRole) {
        Account newAccount = Account.builder()
                .email(email)
                .fullName(fullName)
                .password(password)
                .role(Role.CLIENT.getId())
                .subRole(subRole.getId())
                .isNormalLogin(false)
                .build();
        return accountRepository.saveAndFlush(newAccount);
    }

    public LoginInfoDto proxyLogin(String accountId, String accountType) {
        Role role = Role.fromId(accountType);
        Optional<Account> loggedAccountOp = accountRepository.findByIdAndRole(accountId, role.getId());
        if (loggedAccountOp.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.ACCOUNT.getTargetName(),
                            accountId))
                    .fieldError("id")
                    .build());
        }

        Account loggedAccount = loggedAccountOp.get();
        Account account = GeneralUtils.getCurrentUser();
        List<String> permissions = getPermissions(account);

        return LoginInfoDto.builder()
                .email(loggedAccount.getEmail())
                .fullName(loggedAccount.getFullName())
                .accessToken(jwtService.generateToken(loggedAccount, account.getEmail(), true, permissions))
                .role(loggedAccount.getRole())
                .permissions(permissions)
                .build();
    }

    /**
     * Get permissions by CLIENT account.
     * <p>
     * <p>
     * Logic: return the list of permissions of the target account
     * <p>
     * - if the target account is Client, return the permissions of the current
     * account.
     * <p>
     *
     * @param targetAccount  Account
     * @return List<String> | null
     */
    public List<String> getPermissions(Account targetAccount) {
        List<String> permissions = ScreenPermission.getScreenPermissions();

        if (targetAccount.getRole().equals(Role.OPERATOR.getId())) {
            OperatorAccount opAccount = utilService.getExistingOperatorAccountById(targetAccount.getId());
            permissions = opAccount.getPermissions();
        } else if (targetAccount.getRole().equals(Role.OEM.getId())) {
            OemAccount oemAccount = utilService.getExistingOemAccountById(targetAccount.getId());
            permissions = oemAccount.getPermissions();
        }

        return permissions;
    }
}
