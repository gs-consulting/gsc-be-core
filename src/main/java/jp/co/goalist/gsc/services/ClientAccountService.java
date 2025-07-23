package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.dtos.OemClientAccountItemsDto;
import jp.co.goalist.gsc.dtos.applicant.ApplicantClientAccountCountDto;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.EmploymentType;
import jp.co.goalist.gsc.enums.Role;
import jp.co.goalist.gsc.enums.SubRole;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.*;
import jp.co.goalist.gsc.services.criteriaBuilder.OemClientAccountCriteriaBuilder;
import jp.co.goalist.gsc.utils.GeneralUtils;
import jp.co.goalist.gsc.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static jp.co.goalist.gsc.mappers.ClientAccountMapper.CLIENT_ACCOUNT_MAPPER;
import static jp.co.goalist.gsc.mappers.DropdownMapper.DROPDOWN_MAPPER;

@Service
@RequiredArgsConstructor
public class ClientAccountService {

    private final OemClientAccountRepository oemClientAccountRepository;
    private final OperatorClientAccountRepository operatorClientAccountRepository;
    private final OemApplicantRepository oemApplicantRepository;
    private final OperatorApplicantRepository operatorApplicantRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final OemService oemService;
    private final StoreService storeService;
    private final BranchService branchService;
    private final UtilService utilService;
    private final OemGroupRepository oemGroupRepository;
    private final OemClientAccountCriteriaBuilder oemClientAccountCriteriaBuilder;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createClientAccount(ClientAccountUpsertDto clientAccountUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();

        switch (Role.fromId(account.getRole())) {
            case Role.OPERATOR -> {
                validateOperatorClientUpsertDto(clientAccountUpsertDto);
                validateEmailDomainChange(null, clientAccountUpsertDto.getDomainSetting(), true);
                String temporaryPassword = PasswordGenerator.generatePassword();
                Account newAccount = accountService.createNewClientAccount(
                        "client_email_" + temporaryPassword,
                        clientAccountUpsertDto.getClientName(),
                        passwordEncoder.encode(temporaryPassword),
                        SubRole.OPERATOR
                );
                newAccount.setEnabled(true);

                OperatorClientAccount operatorClientAccount = CLIENT_ACCOUNT_MAPPER.toOperatorClientAccount(clientAccountUpsertDto);
                updateOperatorRelationships(
                        clientAccountUpsertDto.getPrefecture(),
                        clientAccountUpsertDto.getCity(),
                        clientAccountUpsertDto.getBranchIds(),
                        clientAccountUpsertDto.getStoreIds(),
                        clientAccountUpsertDto.getManagerIds(),
                        null,
                        operatorClientAccount);
                operatorClientAccount.setId(newAccount.getId());

                operatorClientAccountRepository.saveAndFlush(operatorClientAccount);
            }
            case Role.OEM -> {
                validateEmailDomainChange(null, clientAccountUpsertDto.getDomainSetting(), false);
                OemAccount oemAccount = oemService.getExistingById(account.getId());
                String temporaryPassword = PasswordGenerator.generatePassword();
                Account newAccount = accountService.createNewClientAccount(
                        "client_email_" + temporaryPassword,
                        clientAccountUpsertDto.getClientName(),
                        passwordEncoder.encode(temporaryPassword),
                        SubRole.OEM
                );
                newAccount.setEnabled(true);

                OemClientAccount oemClientAccount = CLIENT_ACCOUNT_MAPPER.toOemClientAccount(clientAccountUpsertDto);
                updateOemRelationships(
                        clientAccountUpsertDto.getPrefecture(),
                        clientAccountUpsertDto.getCity(),
                        clientAccountUpsertDto.getBranchIds(),
                        clientAccountUpsertDto.getStoreIds(),
                        clientAccountUpsertDto.getManagerIds(),
                        null,
                        oemClientAccount);
                oemClientAccount.setOemGroupId(oemAccount.getOemGroup().getId());
                oemClientAccount.setOemAccount(oemAccount);
                oemClientAccount.setId(newAccount.getId());

                oemClientAccountRepository.saveAndFlush(oemClientAccount);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }

        // publish event to send email
//        utilService.publishMailRegisterEvent(clientAccountUpsertDto.getEmail());
    }

    @Transactional
    public void editClientAccount(String accountId, ClientAccountUpsertDto clientAccountUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();

        switch (Role.fromId(account.getRole())) {
            case Role.OPERATOR -> {
                validateOperatorClientUpsertDto(clientAccountUpsertDto);
                OperatorClientAccount operatorClientAccount = utilService.getExistingOperatorClientAccountById(accountId);
                validateEmailDomainChange(operatorClientAccount.getDomainSetting(), clientAccountUpsertDto.getDomainSetting(), true);

                CLIENT_ACCOUNT_MAPPER.updateOperatorClientAccount(operatorClientAccount, clientAccountUpsertDto);
                operatorClientAccount.getAccount().setFullName(clientAccountUpsertDto.getClientName());
                updateOperatorRelationships(
                        clientAccountUpsertDto.getPrefecture(),
                        clientAccountUpsertDto.getCity(),
                        clientAccountUpsertDto.getBranchIds(),
                        clientAccountUpsertDto.getStoreIds(),
                        clientAccountUpsertDto.getManagerIds(),
                        null,
                        operatorClientAccount);

                operatorClientAccountRepository.saveAndFlush(operatorClientAccount);
            }
            case Role.OEM -> {
                Optional<OemClientAccount> optionalOne = oemClientAccountRepository.findById(accountId);
                if (optionalOne.isPresent()) {
                    OemClientAccount oemClientAccount = optionalOne.get();
                    validateEmailDomainChange(oemClientAccount.getDomainSetting(), clientAccountUpsertDto.getDomainSetting(), false);

                    CLIENT_ACCOUNT_MAPPER.updateOemClientAccount(oemClientAccount, clientAccountUpsertDto);
                    oemClientAccount.getAccount().setFullName(clientAccountUpsertDto.getClientName());
                    updateOemRelationships(
                            clientAccountUpsertDto.getPrefecture(),
                            clientAccountUpsertDto.getCity(),
                            clientAccountUpsertDto.getBranchIds(),
                            clientAccountUpsertDto.getStoreIds(),
                            clientAccountUpsertDto.getManagerIds(),
                            null,
                            oemClientAccount);

                    oemClientAccountRepository.saveAndFlush(oemClientAccount);
                } else {
                    OperatorClientAccount operatorClientAccount = utilService.getExistingOperatorClientAccountById(accountId);
                    validateEmailDomainChange(operatorClientAccount.getDomainSetting(), clientAccountUpsertDto.getDomainSetting(), true);

                    CLIENT_ACCOUNT_MAPPER.updateOperatorClientAccountForOem(operatorClientAccount, clientAccountUpsertDto);
                    operatorClientAccount.getAccount().setFullName(clientAccountUpsertDto.getClientName());
                    updateOperatorRelationships(
                            clientAccountUpsertDto.getPrefecture(),
                            clientAccountUpsertDto.getCity(),
                            clientAccountUpsertDto.getBranchIds(),
                            clientAccountUpsertDto.getStoreIds(),
                            clientAccountUpsertDto.getManagerIds(),
                            null,
                            operatorClientAccount);

                    operatorClientAccountRepository.saveAndFlush(operatorClientAccount);
                }
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public ClientAccountDetailsDto getClientAccountDetails(String accountId) {
        Account account = GeneralUtils.getCurrentUser();
        switch (Role.fromId(account.getRole())) {
            case Role.OPERATOR -> {
                OperatorClientAccount operatorClientAccount = utilService.getExistingOperatorClientAccountById(accountId);
                return CLIENT_ACCOUNT_MAPPER.toOperatorClientAccountDetailsDto(operatorClientAccount);
            }
            case Role.OEM -> {
                return getExistingOemClientAccountByIdForOemRole(accountId);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    /***
     * When OEM logs in, they can see the list of client account which managed by OEM account
     * and the list of client account which managed by Operator account, but belongs to this OEM group as well
     * <p>
     * When Operator logs in, they can the list of client account which managed by Operator account only
     * @param pageNumber the current page number
     * @param pageSize the limit of items for one page
     * @param searchInput the key word for searching
     * @return list of client accounts
     */
    @Transactional
    public ClientAccountListDto getClientAccounts(Integer pageNumber, Integer pageSize, String searchInput) {
        Account account = GeneralUtils.getCurrentUser();
        Triple<String, String, String> parentInfo = utilService.getParentIdAndOemIdAndGroupId(account);

        switch (Role.fromId(account.getRole())) {
            case Role.OPERATOR -> {
                Page<OperatorClientAccount> clientAccounts = operatorClientAccountRepository.findAllBy(
                        null,
                        GeneralUtils.wrapToLike(searchInput),
                        GeneralUtils.getPagination(pageNumber, pageSize)
                );

                Map<String, List<ApplicantClientAccountCountDto>> applicationCountMap =
                        operatorApplicantRepository.countAllApplicationsForClientList(null)
                                .stream().collect(Collectors.groupingBy(ApplicantClientAccountCountDto::getParentId));

                return ClientAccountListDto.builder()
                        .page(clientAccounts.getNumber() + 1)
                        .limit(clientAccounts.getSize())
                        .total(clientAccounts.getTotalElements())
                        .items(clientAccounts.getContent().stream().map(i -> {
                                    Map<String, ApplicantClientAccountCountDto> groupByBranchId = getByBranchId(applicationCountMap, i.getId());
                                    Set<OperatorBranch> branches = i.getLocations().stream()
                                            .map(OperatorClientLocation::getOperatorBranch).filter(Objects::nonNull)
                                            .collect(Collectors.toSet());

                                    Pair<Long, Long> sumAllCount = sumAllOperatorApplicantCountByBranchIds(
                                            branches,
                                            groupByBranchId
                                    );
                                    return CLIENT_ACCOUNT_MAPPER.toOperatorClientAccountItemsDto(
                                            i,
                                            sumAllCount.getLeft(),
                                            sumAllCount.getRight()
                                    );
                                })
                                .collect(Collectors.toList()))
                        .build();
            }
            case Role.OEM -> {
                Page<OemClientAccountItemsDto> clientAccounts = oemClientAccountCriteriaBuilder.findAllProjectsByConditions(
                        parentInfo.getMiddle(),
                        parentInfo.getRight(),
                        searchInput,
                        pageNumber, pageSize
                );

                // OEM account don't have parent_id info of client account's data
                Map<String, List<ApplicantClientAccountCountDto>> applicationCountMap =
                        oemApplicantRepository.countAllApplicationsForClientListForOemList(parentInfo.getRight())
                                .stream().collect(Collectors.groupingBy(ApplicantClientAccountCountDto::getParentId));

                return ClientAccountListDto.builder()
                        .page(clientAccounts.getNumber() + 1)
                        .limit(clientAccounts.getSize())
                        .total(clientAccounts.getTotalElements())
                        .items(clientAccounts.getContent().stream().map(i -> {
                                    Map<String, ApplicantClientAccountCountDto> groupByBranchId = getByBranchId(applicationCountMap, i.getId());
                                    Set<String> branchIds = new HashSet<>();
                                    if (Objects.nonNull(i.getBranchIds())) {
                                        branchIds = new HashSet<>(Arrays.asList(i.getBranchIds().split(",")));
                                    }

                                    Pair<Long, Long> sumAllCount = sumAllOemApplicantCountByBranchIdsForOemRole(
                                            branchIds,
                                            groupByBranchId
                                    );
                                    return CLIENT_ACCOUNT_MAPPER.toOemClientAccountItemsDto(
                                            i,
                                            sumAllCount.getLeft(),
                                            sumAllCount.getRight()
                                    );
                                })
                                .collect(Collectors.toList()))
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public MDropdownListDto getOperatorClients(Integer pageNumber, Integer pageSize, String managerId, String branchId, String storeId) {
        Page<OperatorClientAccount> accounts = operatorClientAccountRepository.findAllEnabledAccounts(
                managerId,
                branchId,
                storeId,
                GeneralUtils.getPagination(pageNumber, pageSize));
        return MDropdownListDto.builder()
                .total(accounts.getTotalElements())
                .limit(accounts.getSize())
                .page(accounts.getNumber() + 1)
                .items(accounts.getContent().stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    public MDropdownListDto getOemClients(Integer pageNumber, Integer pageSize, String managerId, String branchId, String storeId) {
        Page<OemClientAccount> accounts = oemClientAccountRepository.findAllOemClientAccountsDropdown(
                managerId,
                branchId,
                storeId,
                GeneralUtils.getPagination(pageNumber, pageSize));
        return MDropdownListDto.builder()
                .total(accounts.getTotalElements())
                .limit(accounts.getSize())
                .page(accounts.getNumber() + 1)
                .items(accounts.getContent().stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void createClientUserAccount(ClientUserAccountUpsertDto clientUserAccountUpsertDto) {
        accountService.checkDuplicateEmail(clientUserAccountUpsertDto.getEmail());
        GeneralUtils.validatePermissions(clientUserAccountUpsertDto.getPermissions());
        Account account = GeneralUtils.getCurrentUser();
        Account newAccount;

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                OperatorClientAccount parent = utilService.getOperatorParent(account);

                String temporaryPassword = PasswordGenerator.generatePassword();
                newAccount = accountService.createNewAccount(
                        clientUserAccountUpsertDto.getEmail(),
                        clientUserAccountUpsertDto.getFullName(),
                        temporaryPassword,
                        GeneralUtils.randomTokenString(),
                        Role.CLIENT,
                        Role.OPERATOR
                );

                OperatorClientAccount operatorClientAccount = CLIENT_ACCOUNT_MAPPER.toOperatorClientAccount(clientUserAccountUpsertDto);
                updateOperatorRelationships(
                        null,
                        null,
                        clientUserAccountUpsertDto.getBranchIds(),
                        clientUserAccountUpsertDto.getStoreIds(),
                        null,
                        clientUserAccountUpsertDto.getEmploymentType(),
                        operatorClientAccount);
                operatorClientAccount.setId(newAccount.getId());
                operatorClientAccount.setParent(parent);
                operatorClientAccount.setClientName(clientUserAccountUpsertDto.getFullName());
                operatorClientAccount.setFuriganaName(clientUserAccountUpsertDto.getFuriganaName());
                operatorClientAccountRepository.saveAndFlush(operatorClientAccount);
            }
            case Role.OEM -> {
                OemClientAccount parent = utilService.getOemParent(account);
                String temporaryPassword = PasswordGenerator.generatePassword();
                newAccount = accountService.createNewAccount(
                        clientUserAccountUpsertDto.getEmail(),
                        clientUserAccountUpsertDto.getFullName(),
                        temporaryPassword,
                        GeneralUtils.randomTokenString(),
                        Role.CLIENT,
                        Role.OEM
                );

                OemClientAccount newClientAccount = CLIENT_ACCOUNT_MAPPER.toOemClientAccount(clientUserAccountUpsertDto);
                updateOemRelationships(
                        null,
                        null,
                        clientUserAccountUpsertDto.getBranchIds(),
                        clientUserAccountUpsertDto.getStoreIds(),
                        null,
                        clientUserAccountUpsertDto.getEmploymentType(),
                        newClientAccount);
                newClientAccount.setOemGroupId(parent.getOemGroupId());
                newClientAccount.setParent(parent);
                newClientAccount.setOemAccount(newClientAccount.getParent().getOemAccount());
                newClientAccount.setId(newAccount.getId());
                newClientAccount.setClientName(clientUserAccountUpsertDto.getFullName());
                newClientAccount.setFuriganaName(clientUserAccountUpsertDto.getFuriganaName());

                oemClientAccountRepository.saveAndFlush(newClientAccount);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }

        // publish event to send email
        utilService.publishMailRegisterEvent(newAccount.getEmail());
    }

    @Transactional
    public void editUserClientAccount(String accountId, ClientUserAccountUpsertDto clientUserAccountUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        GeneralUtils.validatePermissions(clientUserAccountUpsertDto.getPermissions());

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                OperatorClientAccount operatorClientAccount = utilService.getExistingOperatorClientAccountById(accountId);
                GeneralUtils.validateEmailChange(operatorClientAccount.getAccount().getEmail(), clientUserAccountUpsertDto.getEmail());

                CLIENT_ACCOUNT_MAPPER.updateOperatorUserClientAccount(operatorClientAccount, clientUserAccountUpsertDto);
                updateOperatorRelationships(
                        null,
                        null,
                        clientUserAccountUpsertDto.getBranchIds(),
                        clientUserAccountUpsertDto.getStoreIds(),
                        null,
                        clientUserAccountUpsertDto.getEmploymentType(),
                        operatorClientAccount);

                operatorClientAccountRepository.saveAndFlush(operatorClientAccount);

                operatorClientAccount.getAccount().setFullName(clientUserAccountUpsertDto.getFullName());
                accountRepository.saveAndFlush(operatorClientAccount.getAccount());
            }
            case Role.OEM -> {
                OemClientAccount oemClientAccount = utilService.getExistingOemClientAccountById(accountId);
                GeneralUtils.validateEmailChange(oemClientAccount.getAccount().getEmail(), clientUserAccountUpsertDto.getEmail());

                CLIENT_ACCOUNT_MAPPER.updateOemUserClientAccount(oemClientAccount, clientUserAccountUpsertDto);
                updateOemRelationships(
                        null,
                        null,
                        clientUserAccountUpsertDto.getBranchIds(),
                        clientUserAccountUpsertDto.getStoreIds(),
                        null,
                        clientUserAccountUpsertDto.getEmploymentType(),
                        oemClientAccount);

                oemClientAccountRepository.saveAndFlush(oemClientAccount);

                oemClientAccount.getAccount().setFullName(clientUserAccountUpsertDto.getFullName());
                accountRepository.saveAndFlush(oemClientAccount.getAccount());
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public ClientUserAccountDetailsDto getUserClientAccountDetails(String accountId) {
        Account account = GeneralUtils.getCurrentUser();
        ClientUserAccountDetailsDto clientAccountDetailsDto;

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                OperatorClientAccount operatorClientAccount = utilService.getExistingOperatorClientAccountById(accountId);
                clientAccountDetailsDto = CLIENT_ACCOUNT_MAPPER.toOperatorClientUserAccountDetailsDto(operatorClientAccount);
                clientAccountDetailsDto.setIsRestricted(Objects.equals(operatorClientAccount.getAccount().getId(), account.getId()));
            }
            case Role.OEM -> {
                OemClientAccount oemClientAccount = utilService.getExistingOemClientAccountById(accountId);
                clientAccountDetailsDto = CLIENT_ACCOUNT_MAPPER.toOemClientUserAccountDetailsDto(oemClientAccount);
                clientAccountDetailsDto.setIsRestricted(Objects.equals(oemClientAccount.getAccount().getId(), account.getId()));
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
        return clientAccountDetailsDto;
    }

    @Transactional
    public ClientUserAccountListDto getUserClientAccounts(Integer pageNumber, Integer pageSize, String searchInput) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                Page<OperatorClientAccount> clientAccounts = operatorClientAccountRepository.findAllInternalUsersBy(
                        parentInfo.getLeft(),
                        GeneralUtils.wrapToLike(searchInput),
                        GeneralUtils.getPagination(pageNumber, pageSize)
                );

                Map<String, ApplicantClientAccountCountDto> applicationCountMap =
                        operatorApplicantRepository.countAllApplicationsForClientList(parentInfo.getLeft())
                                .stream().collect(Collectors.toMap(ApplicantClientAccountCountDto::getBranchId, s -> s));

                return ClientUserAccountListDto.builder()
                        .page(clientAccounts.getNumber() + 1)
                        .limit(clientAccounts.getSize())
                        .total(clientAccounts.getTotalElements())
                        .items(clientAccounts.getContent().stream().map(i -> {
                                    Set<OperatorBranch> branches = i.getLocations().stream()
                                            .map(OperatorClientLocation::getOperatorBranch).filter(Objects::nonNull)
                                            .collect(Collectors.toSet());

                                    Pair<Long, Long> sumAllCount = sumAllOperatorApplicantCountByBranchIds(
                                            branches,
                                            applicationCountMap
                                    );

                                    return CLIENT_ACCOUNT_MAPPER.toOperatorClientUserAccountItemsDto(
                                            i,
                                            sumAllCount.getLeft(),
                                            sumAllCount.getRight()
                                    );
                                })
                                .collect(Collectors.toList()))
                        .build();
            }
            case SubRole.OEM -> {
                Page<OemClientAccount> clientAccounts = oemClientAccountRepository.findAllInternalUsersBy(
                        parentInfo.getLeft(),
                        parentInfo.getRight(),
                        GeneralUtils.wrapToLike(searchInput),
                        GeneralUtils.getPagination(pageNumber, pageSize)
                );

                Map<String, ApplicantClientAccountCountDto> applicationCountMap =
                        oemApplicantRepository.countAllApplicationsForClientList(parentInfo.getLeft(), parentInfo.getRight())
                                .stream().collect(Collectors.toMap(ApplicantClientAccountCountDto::getBranchId, s -> s));

                return ClientUserAccountListDto.builder()
                        .page(clientAccounts.getNumber() + 1)
                        .limit(clientAccounts.getSize())
                        .total(clientAccounts.getTotalElements())
                        .items(clientAccounts.getContent().stream().map(i -> {
                                    Set<OemBranch> branches = i.getLocations().stream()
                                            .map(OemClientLocation::getOemBranch).filter(Objects::nonNull)
                                            .collect(Collectors.toSet());

                                    Pair<Long, Long> sumAllCount = sumAllOemApplicantCountByBranchIds(
                                            branches,
                                            applicationCountMap
                                    );
                                    return CLIENT_ACCOUNT_MAPPER.toOemClientUserAccountItemsDto(
                                            i,
                                            sumAllCount.getLeft(),
                                            sumAllCount.getRight()
                                    );
                                })
                                .collect(Collectors.toList()))
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public MDropdownListDto getClientAccountsDropdown(Boolean isInterviewer) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                List<OperatorClientAccount> clientAccounts = operatorClientAccountRepository.findAllForDropdown(
                        parentInfo.getLeft(),
                        isInterviewer
                );
                return MDropdownListDto.builder()
                        .items(clientAccounts.stream().map(CLIENT_ACCOUNT_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                        .build();
            }
            case Role.OEM -> {
                List<OemClientAccount> clientAccounts = oemClientAccountRepository.findAllForDropdown(
                        parentInfo.getLeft(),
                        parentInfo.getRight(),
                        isInterviewer
                );
                return MDropdownListDto.builder()
                        .items(clientAccounts.stream().map(CLIENT_ACCOUNT_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    private void updateOemRelationships(String prefectureId,
                                        String cityId,
                                        List<String> branchIds,
                                        List<String> storeIds,
                                        List<String> managerIds,
                                        String employmentType,
                                        OemClientAccount oemClientAccount) {
        if (Objects.nonNull(employmentType)) {
            EmploymentType type = EmploymentType.fromId(employmentType);
            oemClientAccount.setEmploymentType(type.getId());
        } else {
            oemClientAccount.setEmploymentType(null);
        }

        Pair<Prefecture, City> prefectureCityPair = utilService.findPrefectureAndCity(
                prefectureId,
                cityId
        );

        oemClientAccount.setPrefecture(prefectureCityPair.getLeft());
        oemClientAccount.setCity(prefectureCityPair.getRight());

        if (Objects.isNull(managerIds) || managerIds.isEmpty()) {
            oemClientAccount.setManagers(null);
        } else {
            List<OemClientAccount> managers = oemClientAccountRepository.findAllEnabledAccountsByIds(managerIds);
            if (!Objects.equals(managers.size(), managerIds.size())) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message(String.format(ErrorMessage.INVALID_DATA.getMessage(), "営業担当者"))
                        .fieldError("managerIds")
                        .build());
            }
            oemClientAccount.setManagers(managers);
        }

        // 選んだ支店のみ
        if (Objects.isNull(storeIds) || storeIds.isEmpty()) {
            List<OemClientLocation> locations = returnOemBranchList(branchIds, oemClientAccount);
            if (!locations.isEmpty()) {
                oemClientAccount.setLocations(locations);
            } else {
                oemClientAccount.setLocations(null);
            }

        } else {
            if (Objects.isNull(branchIds) || branchIds.isEmpty()) {
                throw new BadValidationException(
                        ErrorResponse.builder()
                                .statusCode(ErrorMessage.REQUIRED_FIELD.getStatusCode())
                                .message(String.format(ErrorMessage.REQUIRED_FIELD.getMessage(),
                                        TargetName.BRANCH.getTargetName()))
                                .fieldError("branchIds")
                                .build()
                );
            }

            List<OemStore> stores = storeService.getOemStoresByIdsAndBranchIds(storeIds, branchIds);
            if (!Objects.equals(stores.size(), storeIds.size())) {
                throw new BadValidationException(
                        ErrorResponse.builder()
                                .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                        TargetName.STORE.getTargetName()))
                                .fieldError("storeIds")
                                .build()
                );
            }

            List<OemClientLocation> locations = new ArrayList<>();

            // 拠点と選んだ支店
            if (!stores.isEmpty()) {
                stores.forEach(store -> {
                    OemClientLocation newLocation = new OemClientLocation();
                    newLocation.setAccount(oemClientAccount);
                    newLocation.setOemBranch(store.getBranch());
                    newLocation.setOemStore(store);
                    locations.add(newLocation);
                });
            }

            // 選んだ支店のみ
            List<OemClientLocation> branches = returnOemBranchList(branchIds, oemClientAccount);
            if (!branches.isEmpty()) {
                locations.addAll(branches);
            }

            if (locations.isEmpty()) {
                oemClientAccount.setLocations(null);
            } else {
                oemClientAccount.setLocations(locations);
            }
        }
    }

    private void updateOperatorRelationships(String prefectureId,
                                             String cityId,
                                             List<String> branchIds,
                                             List<String> storeIds,
                                             List<String> managerIds,
                                             String employmentType,
                                             OperatorClientAccount operatorClientAccount) {
        if (Objects.nonNull(employmentType)) {
            EmploymentType type = EmploymentType.fromId(employmentType);
            operatorClientAccount.setEmploymentType(type.getId());
        } else {
            operatorClientAccount.setEmploymentType(null);
        }

        Pair<Prefecture, City> prefectureCityPair = utilService.findPrefectureAndCity(
                prefectureId,
                cityId
        );

        operatorClientAccount.setPrefecture(prefectureCityPair.getLeft());
        operatorClientAccount.setCity(prefectureCityPair.getRight());

        if (Objects.isNull(managerIds) || managerIds.isEmpty()) {
            operatorClientAccount.setManagers(null);
        } else {
            List<OperatorClientAccount> managers = operatorClientAccountRepository.findAllEnabledAccountsByIds(managerIds);
            if (!Objects.equals(managers.size(), managerIds.size())) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message(String.format(ErrorMessage.INVALID_DATA.getMessage(), "営業担当者"))
                        .fieldError("managerIds")
                        .build());
            }
            operatorClientAccount.setManagers(managers);
        }

        // 選んだ支店のみ
        if (Objects.isNull(storeIds) || storeIds.isEmpty()) {
            List<OperatorClientLocation> locations = returnOperatorBranchList(branchIds, operatorClientAccount);
            if (!locations.isEmpty()) {
                operatorClientAccount.setLocations(locations);
            } else {
                operatorClientAccount.setLocations(null);
            }

        } else {
            if (Objects.isNull(branchIds) || branchIds.isEmpty()) {
                throw new BadValidationException(
                        ErrorResponse.builder()
                                .statusCode(ErrorMessage.REQUIRED_FIELD.getStatusCode())
                                .message(String.format(ErrorMessage.REQUIRED_FIELD.getMessage(),
                                        TargetName.BRANCH.getTargetName()))
                                .fieldError("branchIds")
                                .build()
                );
            }

            List<OperatorStore> stores = storeService.getOperatorStoresByIdsAndBranchIds(storeIds, branchIds);
            if (!Objects.equals(stores.size(), storeIds.size())) {
                throw new BadValidationException(
                        ErrorResponse.builder()
                                .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                        TargetName.STORE.getTargetName()))
                                .fieldError("storeIds")
                                .build()
                );
            }

            List<OperatorClientLocation> locations = new ArrayList<>();

            // 拠点と選んだ支店
            if (!stores.isEmpty()) {
                stores.forEach(store -> {
                    OperatorClientLocation newLocation = new OperatorClientLocation();
                    newLocation.setAccount(operatorClientAccount);
                    newLocation.setOperatorBranch(store.getBranch());
                    newLocation.setOperatorStore(store);
                    locations.add(newLocation);
                });
            }

            // 選んだ支店のみ
            List<OperatorClientLocation> branches = returnOperatorBranchList(branchIds, operatorClientAccount);
            if (!branches.isEmpty()) {
                locations.addAll(branches);
            }

            if (locations.isEmpty()) {
                operatorClientAccount.setLocations(null);
            } else {
                operatorClientAccount.setLocations(locations);
            }
        }
    }

    private List<OperatorClientLocation> returnOperatorBranchList(List<String> branchIds, OperatorClientAccount account) {
        if (Objects.nonNull(branchIds) && !branchIds.isEmpty()) {
            List<OperatorBranch> branches = branchService.getExistingOperatorBranchByIds(branchIds);

            if (!Objects.equals(branches.size(), branchIds.size())) {
                throw new BadValidationException(
                        ErrorResponse.builder()
                                .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                        TargetName.BRANCH.getTargetName()))
                                .fieldError("branchIds")
                                .build()
                );
            }

            return branches.stream().map(branch -> {
                OperatorClientLocation newLocation = new OperatorClientLocation();
                newLocation.setAccount(account);
                newLocation.setOperatorBranch(branch);
                return newLocation;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<OemClientLocation> returnOemBranchList(List<String> branchIds, OemClientAccount account) {
        if (Objects.nonNull(branchIds) && !branchIds.isEmpty()) {
            List<OemBranch> branches = branchService.getExistingOemBranchByIds(branchIds);

            if (!Objects.equals(branches.size(), branchIds.size())) {
                throw new BadValidationException(
                        ErrorResponse.builder()
                                .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                .message(String.format(ErrorMessage.INVALID_DATA.getMessage(), TargetName.BRANCH))
                                .fieldError("branchIds")
                                .build()
                );
            }

            return branches.stream().map(branch -> {
                OemClientLocation newLocation = new OemClientLocation();
                newLocation.setAccount(account);
                newLocation.setOemBranch(branch);
                return newLocation;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private void validateOperatorClientUpsertDto(ClientAccountUpsertDto clientAccountUpsertDto) {
        if (Objects.isNull(clientAccountUpsertDto.getOemGroupId())) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.REQUIRED_FIELD.getStatusCode())
                        .message(String.format(ErrorMessage.REQUIRED_FIELD.getMessage(),
                                "OEM"))
                        .fieldError("oemGroupId")
                        .build());
        }

        if (!Objects.equals(clientAccountUpsertDto.getOemGroupId(), Constants.GS_OEM_DEFAULT_ID)) {
            Optional<OemGroup> oemGroup = oemGroupRepository.findById(clientAccountUpsertDto.getOemGroupId());
            if (oemGroup.isEmpty()) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                "OEM"))
                        .fieldError("oemGroupId")
                        .build());
            }
        }
    }

    private void validateEmailDomainChange(String currentOne, String updateOne, boolean isOperator) {
        if (Objects.isNull(currentOne) && Objects.nonNull(updateOne)) {
            if (isOperator) {
                Optional<OperatorClientAccount> checkDomainSetting = operatorClientAccountRepository.findParentClientDomain(updateOne);
                if (checkDomainSetting.isPresent()) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.MAIL_DOMAIN_SETTING_DUPLICATE.getStatusCode())
                            .message(String.format(ErrorMessage.MAIL_DOMAIN_SETTING_DUPLICATE.getMessage(),
                                    updateOne))
                            .build());
                }
            } else {
                Optional<OemClientAccount> checkDomainSetting = oemClientAccountRepository.findParentClientDomain(updateOne);
                if (checkDomainSetting.isPresent()) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.MAIL_DOMAIN_SETTING_DUPLICATE.getStatusCode())
                            .message(String.format(ErrorMessage.MAIL_DOMAIN_SETTING_DUPLICATE.getMessage(),
                                    updateOne))
                            .build());
                }
            }
        } else if (!Objects.equals(currentOne, updateOne)) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.EMAIL_DOMAIN_CHANGED.getStatusCode())
                    .message(ErrorMessage.EMAIL_DOMAIN_CHANGED.getMessage())
                    .fieldError("domainSetting")
                    .build());
        }
    }

    private Pair<Long, Long> sumAllOperatorApplicantCountByBranchIds(Set<OperatorBranch> branches,
                                                                     Map<String, ApplicantClientAccountCountDto> applicationCountMap) {
        long totalApplicants = 0, totalUnResponseApplicants = 0;
        if (applicationCountMap.isEmpty()) {
            return Pair.of(totalApplicants, totalUnResponseApplicants);
        }

        for (OperatorBranch branch : branches) {
            ApplicantClientAccountCountDto countDto = applicationCountMap.get(branch.getId());
            if (Objects.nonNull(countDto)) {
                totalApplicants += countDto.getTotalApplicants();
                totalUnResponseApplicants += countDto.getTotalUnResponseApplicants();
            }
        }

        return Pair.of(totalApplicants, totalUnResponseApplicants);
    }

    private Pair<Long, Long> sumAllOemApplicantCountByBranchIdsForOemRole(Set<String> branchIds,
                                                                Map<String, ApplicantClientAccountCountDto> applicationCountMap) {
        long totalApplicants = 0, totalUnResponseApplicants = 0;
        if (applicationCountMap.isEmpty()) {
            return Pair.of(totalApplicants, totalUnResponseApplicants);
        }

        for (String branchId : branchIds) {
            ApplicantClientAccountCountDto countDto = applicationCountMap.get(branchId);
            if (Objects.nonNull(countDto)) {
                totalApplicants += countDto.getTotalApplicants();
                totalUnResponseApplicants += countDto.getTotalUnResponseApplicants();
            }
        }

        return Pair.of(totalApplicants, totalUnResponseApplicants);
    }

    private Pair<Long, Long> sumAllOemApplicantCountByBranchIds(Set<OemBranch> branches,
                                                                Map<String, ApplicantClientAccountCountDto> applicationCountMap) {
        long totalApplicants = 0, totalUnResponseApplicants = 0;
        if (applicationCountMap.isEmpty()) {
            return Pair.of(totalApplicants, totalUnResponseApplicants);
        }

        for (OemBranch branch : branches) {
            ApplicantClientAccountCountDto countDto = applicationCountMap.get(branch.getId());
            if (Objects.nonNull(countDto)) {
                totalApplicants += countDto.getTotalApplicants();
                totalUnResponseApplicants += countDto.getTotalUnResponseApplicants();
            }
        }

        return Pair.of(totalApplicants, totalUnResponseApplicants);
    }

    private Map<String, ApplicantClientAccountCountDto> getByBranchId(Map<String, List<ApplicantClientAccountCountDto>> applicationCountMap,
                                                                      String parentId) {
        List<ApplicantClientAccountCountDto> filteredByParentId = applicationCountMap.get(parentId);
        Map<String, ApplicantClientAccountCountDto> groupByBranchId = new HashMap<>();
        if (Objects.nonNull(filteredByParentId)) {
            groupByBranchId = filteredByParentId
                    .stream().collect(Collectors.toMap(ApplicantClientAccountCountDto::getBranchId, s -> s));
        }
        return groupByBranchId;
    }

    public ClientAccountDetailsDto getExistingOemClientAccountByIdForOemRole(String id) {
        Optional<OemClientAccount> optionalOemAccount = oemClientAccountRepository.findById(id);
        ClientAccountDetailsDto clientAccountDetailsDto;

        if (optionalOemAccount.isEmpty()) {
            Optional<OperatorClientAccount> optionalOperatorOne = operatorClientAccountRepository.findById(id);

            if (optionalOperatorOne.isEmpty()) {
                throw new NotFoundException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                        .message(String.format(
                                ErrorMessage.NOT_FOUND.getMessage(),
                                TargetName.OPERATOR_ACCOUNT.getTargetName()))
                        .fieldError("id")
                        .build());
            }
            clientAccountDetailsDto = CLIENT_ACCOUNT_MAPPER.toOperatorClientAccountDetailsDto(optionalOperatorOne.get());
            clientAccountDetailsDto.setIsCreatedByOperator(true);
            return clientAccountDetailsDto;
        }
        clientAccountDetailsDto = CLIENT_ACCOUNT_MAPPER.toOemClientAccountDetailsDto(optionalOemAccount.get());
        clientAccountDetailsDto.setIsCreatedByOperator(false);
        return clientAccountDetailsDto;
    }
}
