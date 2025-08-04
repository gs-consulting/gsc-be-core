package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.Role;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.OemAccountRepository;
import jp.co.goalist.gsc.repositories.OemProjectRepository;
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
import static jp.co.goalist.gsc.mappers.OemAccountMapper.OEM_ACCOUNT_MAPPER;

@Service
@RequiredArgsConstructor
public class OemService {

    private final AccountService accountService;
    private final ClientService clientService;
    private final OemAccountRepository oemAccountRepository;
    private final OemProjectRepository oemProjectRepository;
    private final TeamService teamService;
    private final UtilService utilService;

    public OemAccount getExistingByIdAndOemGroupId(String id, String oemGroupId) {
        Optional<OemAccount> optionalOne = oemAccountRepository.findByIdAndOemGroupId(id, oemGroupId);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.OEM_ACCOUNT.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    public OemAccount getExistingById(String id) {
        Optional<OemAccount> optionalOne = oemAccountRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.OEM_ACCOUNT.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    @Transactional
    public void createOemAccount(String id, OemAccountUpsertDto oemAccountUpsertDto) {
        accountService.checkDuplicateEmail(oemAccountUpsertDto.getEmail());

        OemGroup oemGroup = clientService.getExistingById(id);
        String temporaryPassword = PasswordGenerator.generatePassword();
        Account newAccount = accountService.createNewAccount(
                oemAccountUpsertDto.getEmail(),
                oemAccountUpsertDto.getFullName(),
                temporaryPassword,
                GeneralUtils.randomTokenString(),
                Role.OEM,
                null
        );

        OemAccount newOemAccount = OEM_ACCOUNT_MAPPER.toOemAccount(oemAccountUpsertDto);
        newOemAccount.setId(newAccount.getId());
        newOemAccount.setOemGroup(oemGroup);
        updateRelationships(
                oemAccountUpsertDto.getTeamIds(),
                newOemAccount
        );
        oemAccountRepository.save(newOemAccount);

        // publish event to send email
        utilService.publishMailRegisterEvent(newAccount.getEmail());
    }

    @Transactional
    public void editOemAccount(String groupoemGroupId, String oemAccountId, OemAccountUpsertDto oemAccountUpsertDto) {
        OemAccount oemAccount = getExistingByIdAndOemGroupId(oemAccountId, groupoemGroupId);
        GeneralUtils.validateEmailChange(oemAccount.getAccount().getEmail(), oemAccountUpsertDto.getEmail());

        OEM_ACCOUNT_MAPPER.updateToOemAccount(oemAccountUpsertDto, oemAccount);
        oemAccount.getAccount().setFullName(oemAccountUpsertDto.getFullName());
        updateRelationships(
                oemAccountUpsertDto.getTeamIds(),
                oemAccount
        );
        oemAccountRepository.save(oemAccount);
    }

    @Transactional
    public OemAccountDetailsDto getOemAccountDetails(String id, String oemAccountId) {
        OemAccount oemAccount = getExistingById(oemAccountId);
        return OEM_ACCOUNT_MAPPER.toOemAccountDetailsDto(oemAccount);
    }

    @Transactional
    public OemAccountListDto getOemAccounts(String clientGroupId, Integer pageNumber, Integer pageSize, String searchInput) {
        Page<OemAccount> accounts = oemAccountRepository.findAllParentOemBy(
                clientGroupId,
                GeneralUtils.wrapToLike(searchInput),
                GeneralUtils.getPagination(pageNumber, pageSize)
        );

        return OemAccountListDto.builder()
                .total(accounts.getTotalElements())
                .page(accounts.getNumber() + 1)
                .limit(accounts.getSize())
                .items(accounts.getContent()
                        .stream().map(OEM_ACCOUNT_MAPPER::toOemAccountItemsDto).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void createStaffOemAccount(StaffOemAccountUpsertDto staffOemAccountUpsertDto) {
        accountService.checkDuplicateEmail(staffOemAccountUpsertDto.getEmail());

        Account account = GeneralUtils.getCurrentUser();
        OemAccount oemAccount = oemAccountRepository.getReferenceById(account.getId());

        // create account with a random password
        String temporaryPassword = PasswordGenerator.generatePassword();
        Account newAccount = accountService.createNewAccount(
                staffOemAccountUpsertDto.getEmail(),
                staffOemAccountUpsertDto.getFullName(),
                temporaryPassword,
                GeneralUtils.randomTokenString(),
                Role.OEM,
                null
        );

        OemAccount newOemAccount = OEM_ACCOUNT_MAPPER.toStaffOemAccount(staffOemAccountUpsertDto);
        newOemAccount.setParent(Objects.nonNull(oemAccount.getParent()) ? oemAccount.getParent() : oemAccount);
        newOemAccount.setId(newAccount.getId());
        newOemAccount.setOemGroup(oemAccount.getOemGroup());
        updateRelationships(
                staffOemAccountUpsertDto.getTeamIds(),
                newOemAccount
        );
        oemAccountRepository.save(newOemAccount);

        // publish event to send email
        utilService.publishMailRegisterEvent(newAccount.getEmail());
    }

    @Transactional
    public void editStaffOemAccount(String oemAccountId, StaffOemAccountUpsertDto staffOemAccountUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        OemAccount currentOemAccount = getExistingById(account.getId());

        OemAccount oemAccount = getExistingByIdAndOemGroupId(oemAccountId, currentOemAccount.getOemGroup().getId());
        GeneralUtils.validateEmailChange(oemAccount.getAccount().getEmail(), staffOemAccountUpsertDto.getEmail());

        OEM_ACCOUNT_MAPPER.updateToStaffOemAccount(staffOemAccountUpsertDto, oemAccount);
        oemAccount.getAccount().setFullName(staffOemAccountUpsertDto.getFullName());
        updateRelationships(
                staffOemAccountUpsertDto.getTeamIds(),
                oemAccount
        );
        oemAccountRepository.save(oemAccount);
    }

    @Transactional
    public StaffOemAccountDetailsDto getStaffOemAccountDetails(String oemAccountId) {
        OemAccount oemAccount = getExistingById(oemAccountId);
        StaffOemAccountDetailsDto detailsDto = OEM_ACCOUNT_MAPPER.toStaffOemAccountDetailsDto(oemAccount);
        
        Account account = GeneralUtils.getCurrentUser();
        detailsDto.setIsRestricted(account.getId().equals(oemAccount.getId()));
        return detailsDto;
    }

    @Transactional
    public StaffOemAccountListDto getStaffOemAccounts(Integer pageNumber, Integer pageSize, String searchInput) {
        Account currentAccount = GeneralUtils.getCurrentUser();
        OemAccount oemAccount = getExistingById(currentAccount.getId());

        Page<OemAccount> accounts = oemAccountRepository.findAllBy(
                Objects.nonNull(oemAccount.getParent()) ? oemAccount.getParent().getId() : oemAccount.getId(),
                oemAccount.getOemGroup().getId(),
                GeneralUtils.wrapToLike(searchInput),
                GeneralUtils.getPagination(pageNumber, pageSize)
        );

        List<StaffOemAccountItemsDto> items = accounts.getContent().stream()
                .map(acc -> {
                        StaffOemAccountItemsDto dto = OEM_ACCOUNT_MAPPER.toStaffOemAccountItemsDto(acc);
                        boolean isCurrentUser = Objects.equals(acc.getId(), currentAccount.getId());
                        dto.setIsDeletable(!isCurrentUser);
                        return dto;
                })
                .collect(Collectors.toList());

        return StaffOemAccountListDto.builder()
                .page(accounts.getNumber() + 1)
                .limit(accounts.getSize())
                .total(accounts.getTotalElements())
                .items(items)
                .build();
    }

    public MDropdownListDto getOemManagers(Integer pageNumber, Integer pageSize, String teamId) {
        Account account = GeneralUtils.getCurrentUser();
        Page<OemAccount> accounts = oemAccountRepository.findAllEnabledManagersDropdown(
                utilService.getParentIdAndGroupId(account).getLeft(),
                teamId,
                GeneralUtils.getPagination(pageNumber, pageSize)
        );
        return MDropdownListDto.builder()
                .total(accounts.getTotalElements())
                .limit(accounts.getSize())
                .page(accounts.getNumber() + 1)
                .items(accounts.stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    public MDropdownListDto getOemProjects(Integer pageNumber, Integer pageSize, String storeId) {
        Account account = GeneralUtils.getCurrentUser();
        Page<OemProject> projects = oemProjectRepository.findAllProjectsByStoreId(
                utilService.getParentIdAndGroupId(account).getRight(),
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

    public MDropdownListDto getOemStaffs(Integer pageNumber, Integer pageSize) {
        Account account = GeneralUtils.getCurrentUser();
        OemAccount oemAccount = getExistingById(account.getId());

        Page<OemAccount> accounts = oemAccountRepository.findAllBy(
                Objects.nonNull(oemAccount.getParent()) ? oemAccount.getParent().getId() : oemAccount.getId(),
                oemAccount.getOemGroup().getId(),
                null,
                GeneralUtils.getPagination(pageNumber, pageSize));

        return MDropdownListDto.builder()
                .total(accounts.getTotalElements())
                .limit(accounts.getSize())
                .page(accounts.getNumber() + 1)
                .items(accounts.stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    private void updateRelationships(List<String> teamIds, OemAccount oemAccount) {

        if (Objects.isNull(teamIds) || teamIds.isEmpty()) {
            oemAccount.setTeams(null);
        } else {
            List<OemTeam> teams = teamService.getExistingOemTeamByIds(teamIds);
            oemAccount.setTeams(teams);
        }
    }

    @Transactional
    public void deleteSelectedOemStaffs(SelectedIds selectedIds) {
        oemAccountRepository.deleteOemAcountTeamsByIdIn(selectedIds.getSelectedIds());

        List<OemAccount> oemAccounts = oemAccountRepository.findAllById(selectedIds.getSelectedIds());
        oemAccountRepository.deleteAll(oemAccounts);
    }    
}
