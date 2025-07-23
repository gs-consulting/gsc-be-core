package jp.co.goalist.gsc.mocks;

import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.entities.OemAccount;
import jp.co.goalist.gsc.entities.OemApplicant;
import jp.co.goalist.gsc.entities.OemClientAccount;
import jp.co.goalist.gsc.enums.Role;
import jp.co.goalist.gsc.enums.SubRole;
import jp.co.goalist.gsc.repositories.AccountRepository;
import jp.co.goalist.gsc.repositories.OemAccountRepository;
import jp.co.goalist.gsc.repositories.OemApplicantRepository;
import jp.co.goalist.gsc.repositories.OemClientAccountRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MockTestData {

    private final AccountRepository accountRepository;
    private final OemAccountRepository oemAccountRepository;
    private final OemClientAccountRepository oemClientAccountRepository;
    private final OemApplicantRepository oemApplicantRepository;

    public MockTestData(
            AccountRepository accountRepository,
            OemAccountRepository oemAccountRepository,
            OemClientAccountRepository oemClientAccountRepository,
            OemApplicantRepository oemApplicantRepository) {
        this.accountRepository = accountRepository;
        this.oemAccountRepository = oemAccountRepository;
        this.oemClientAccountRepository = oemClientAccountRepository;
        this.oemApplicantRepository = oemApplicantRepository;
    }

    public Account createMockAccount(String email, String fullName, Role role, SubRole subRole) {
        Account account = Account.builder()
                .email(email)
                .fullName(fullName)
                .role(role.getId())
                .subRole(subRole != null ? subRole.getId() : null)
                .enabled(true)
                .build();
        return accountRepository.save(account);
    }

    public OemAccount createMockOemAccount(Account account) {
        OemAccount oemAccount = OemAccount.builder()
                .id(account.getId())
                .account(account)
                .fullName(account.getFullName())
                .build();
        return oemAccountRepository.save(oemAccount);
    }

    public OemClientAccount createMockOemClientAccount(Account account, OemAccount oemAccount) {
        OemClientAccount clientAccount = new OemClientAccount();
        clientAccount.setId(account.getId());
        clientAccount.setAccount(account);
        clientAccount.setOemAccount(oemAccount);
        clientAccount.setClientName(account.getFullName());
        clientAccount.setOemGroupId(UUID.randomUUID().toString());
        return oemClientAccountRepository.save(clientAccount);
    }

    public void createMockOemApplicant(OemClientAccount clientAccount) {
        OemApplicant applicant = new OemApplicant();
        applicant.setParent(clientAccount);
        applicant.setOemGroupId(clientAccount.getOemGroupId());
        applicant.setFullName("Test Applicant");
        applicant.setFuriganaName("テスト応募者");
        applicant.setEmail("applicant@test.com");
        applicant.setTel("0123456789");
        oemApplicantRepository.save(applicant);
    }

    public void createMockDashboardData() {
        // Create OEM account and clients
        Account oemAccount = createMockAccount("oem@test.com", "Test OEM", Role.OEM, null);
        OemAccount oem = createMockOemAccount(oemAccount);

        List<OemClientAccount> oemClientAccounts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Account clientAccount = createMockAccount(
                    "oem.client" + i + "@test.com",
                    "Test OEM Client " + i,
                    Role.CLIENT,
                    SubRole.OEM
            );
            oemClientAccounts.add(createMockOemClientAccount(clientAccount, oem));
        }

        // Create applicants for each OEM client
        for (OemClientAccount clientAccount : oemClientAccounts) {
            for (int i = 0; i < 5; i++) {
                createMockOemApplicant(clientAccount);
            }
        }
    }
}
