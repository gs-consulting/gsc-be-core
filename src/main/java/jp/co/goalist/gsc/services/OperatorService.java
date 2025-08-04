package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.entities.OperatorAccount;
import jp.co.goalist.gsc.entities.OperatorProject;
import jp.co.goalist.gsc.entities.OperatorTeam;
import jp.co.goalist.gsc.enums.Role;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.OperatorAccountRepository;
import jp.co.goalist.gsc.repositories.OperatorProjectRepository;
import jp.co.goalist.gsc.utils.GeneralUtils;
import jp.co.goalist.gsc.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static jp.co.goalist.gsc.mappers.DropdownMapper.DROPDOWN_MAPPER;
import static jp.co.goalist.gsc.mappers.StaffMapper.STAFF_MAPPER;

@Service
@RequiredArgsConstructor
public class OperatorService {

    private final AccountService accountService;
    private final OperatorAccountRepository operatorAccountRepository;
    private final OperatorProjectRepository operatorProjectRepository;
    private final TeamService teamService;
    private final UtilService utilService;

    public OperatorAccount getExistingById(String id) {
        Optional<OperatorAccount> optionalOne = operatorAccountRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.STAFF.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    @Transactional
    public void createStaffOperatorAccount(StaffOperatorAccountUpserDto staffOperatorAccountUpserDto) {
        accountService.checkDuplicateEmail(staffOperatorAccountUpserDto.getEmail());

        // create account with a random password
        String temporaryPassword = PasswordGenerator.generatePassword();
        Account newAccount = accountService.createNewAccount(
                staffOperatorAccountUpserDto.getEmail(),
                staffOperatorAccountUpserDto.getFullName(),
                temporaryPassword,
                GeneralUtils.randomTokenString(),
                Role.OPERATOR,
                null
        );

        OperatorAccount newOperatorAccount = STAFF_MAPPER.toStaffAccount(staffOperatorAccountUpserDto);
        newOperatorAccount.setId(newAccount.getId());
        updateTeam(staffOperatorAccountUpserDto.getTeamIds(), newOperatorAccount);
        operatorAccountRepository.save(newOperatorAccount);

        // publish event to send email
        utilService.publishMailRegisterEvent(newAccount.getEmail());
    }

    @Transactional
    public void editStaffOperatorAccount(String accountId, StaffOperatorAccountUpserDto staffOperatorAccountUpserDto) {
        OperatorAccount operatorAccount = getExistingById(accountId);

        GeneralUtils.validateEmailChange(operatorAccount.getAccount().getEmail(), staffOperatorAccountUpserDto.getEmail());

        STAFF_MAPPER.updateToStaffOemAccount(staffOperatorAccountUpserDto, operatorAccount);
        operatorAccount.getAccount().setFullName(staffOperatorAccountUpserDto.getFullName());
        updateTeam(staffOperatorAccountUpserDto.getTeamIds(), operatorAccount);
        operatorAccountRepository.saveAndFlush(operatorAccount);
    }

    @Transactional
    public StaffOperatorAccountDetailsDto getStaffOperatorAccountDetails(String accountId) {
        OperatorAccount operatorAccount = getExistingById(accountId);
        StaffOperatorAccountDetailsDto detailsDto = STAFF_MAPPER.toOperatorAccountDetailsDto(operatorAccount);

        Account account = GeneralUtils.getCurrentUser();
        detailsDto.setIsRestricted(account.getId().equals(operatorAccount.getId()));
        return detailsDto;
    }

    @Transactional
    public StaffOperatorAccountListDto getStaffOperatorAccounts(Integer pageNumber, Integer pageSize, String searchInput) {
        Account currentAccount = GeneralUtils.getCurrentUser();
        
        Page<OperatorAccount> accounts = operatorAccountRepository.findAllBy(
                GeneralUtils.wrapToLike(searchInput),
                GeneralUtils.getPagination(pageNumber, pageSize)
        );

        List<StaffOperatorAccountItemsDto> items = accounts.getContent().stream()
        .map(acc -> {
                StaffOperatorAccountItemsDto dto = STAFF_MAPPER.toOperatorAccountItemsDto(acc);
                boolean isCurrentUser = Objects.equals(acc.getId(), currentAccount.getId());
                dto.setIsDeletable(!isCurrentUser);
                return dto;
        })
        .collect(Collectors.toList());

        return StaffOperatorAccountListDto.builder()
                .total(accounts.getTotalElements())
                .page(accounts.getNumber() + 1)
                .limit(accounts.getSize())
                .items(items)
                .build();
    }

    public MDropdownListDto getOperatorManagers(Integer pageNumber, Integer pageSize, String teamId) {
        Page<OperatorAccount> accounts = operatorAccountRepository.findAllEnabledManagersDropdown(
                GeneralUtils.getPagination(pageNumber, pageSize),
                teamId
        );
        return MDropdownListDto.builder()
                .total(accounts.getTotalElements())
                .limit(accounts.getSize())
                .page(accounts.getNumber() + 1)
                .items(accounts.getContent().stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    public MDropdownListDto getOperatorProjects(Integer pageNumber, Integer pageSize, String storeId) {
        Page<OperatorProject> projects = operatorProjectRepository.findAllProjectsByStoreId(
                storeId,
                GeneralUtils.getPagination(pageNumber, pageSize)
        );
        return MDropdownListDto.builder()
                .total(projects.getTotalElements())
                .limit(projects.getSize())
                .page(projects.getNumber() + 1)
                .items(projects.stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    public MDropdownListDto getOperatorStaffs(Integer pageNumber, Integer pageSize) {
        Page<OperatorAccount> accounts = operatorAccountRepository.findAllBy(
                null,
                GeneralUtils.getPagination(pageNumber, pageSize)
        );
        return MDropdownListDto.builder()
                .total(accounts.getTotalElements())
                .limit(accounts.getSize())
                .page(accounts.getNumber() + 1)
                .items(accounts.stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    private void updateTeam(List<String> teamIds, OperatorAccount operatorAccount) {
        if (Objects.isNull(teamIds) || teamIds.isEmpty()) {
            operatorAccount.setTeams(null);
        } else {
            List<OperatorTeam> teams = teamService.getExistingOperatorTeamByIds(teamIds);
            operatorAccount.setTeams(teams);
        }
    }

    @Transactional
    public void deleteSelectedOperatorStaffs(SelectedIds selectedIds) {
        operatorAccountRepository.deleteOpAcountTeamsByIdIn(selectedIds.getSelectedIds());

        List<OperatorAccount> operatorAccounts = operatorAccountRepository.findAllById(selectedIds.getSelectedIds());
        operatorAccountRepository.deleteAll(operatorAccounts);
    }
}
