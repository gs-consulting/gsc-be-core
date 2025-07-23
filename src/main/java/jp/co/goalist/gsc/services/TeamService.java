package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.Role;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.*;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static jp.co.goalist.gsc.mappers.DropdownMapper.DROPDOWN_MAPPER;
import static jp.co.goalist.gsc.mappers.TeamMapper.TEAM_MAPPER;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final OperatorTeamRepository operatorTeamRepository;
    private final OemAccountRepository oemAccountRepository;
    private final OemTeamRepository oemTeamRepository;
    private final UtilService utilService;
    private final OperatorAccountRepository operatorAccountRepository;

    public OemTeam getExistingOemTeamById(String id) {
        Optional<OemTeam> optionalOne = oemTeamRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.TEAM.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    public OperatorTeam getExistingOperatorTeamById(String id) {
        Optional<OperatorTeam> optionalOne = operatorTeamRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.TEAM.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    public List<OemTeam> getExistingOemTeamByIds(List<String> ids) {
        return oemTeamRepository.findAllById(ids);
    }

    public List<OperatorTeam> getExistingOperatorTeamByIds(List<String> ids) {
        return operatorTeamRepository.findAllById(ids);
    }

    public List<OemAccount> getExistingOemStaffByIds(List<String> ids) {
        return oemAccountRepository.findAllById(ids);
    }

    public List<OperatorAccount> getExistingOperatorStaffByIds(List<String> ids) {
        return operatorAccountRepository.findAllById(ids);
    }

    private OemAccount getExistingById(String id) {
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
    public void createTeam(TeamUpsertDto teamUpsertDto) {
        // TODO: Cleanup — remove postcode, prefecture_id, city_id, tel, fax_code, email columns since they are no longer in the DTO.
        Account account = GeneralUtils.getCurrentUser();
        if (Objects.equals(account.getRole(), Role.OPERATOR.getId())) {
            validateDuplicateName(null, null, teamUpsertDto.getName());

            OperatorTeam newOperatorTeam = TEAM_MAPPER.mapToOperatorTeam(teamUpsertDto);
            updateOperatorStaff(teamUpsertDto.getStaffIds(), newOperatorTeam);
            operatorTeamRepository.saveAndFlush(newOperatorTeam);
        } else {
            Optional<OemAccount> existing = oemAccountRepository.findById(account.getId());
            if (existing.isEmpty()) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.REQUIRED_FIELD.getStatusCode())
                        .message(String.format(ErrorMessage.REQUIRED_FIELD.getMessage(),
                                TargetName.OEM_ACCOUNT.getTargetName()))
                        .fieldError("oemGroupId")
                        .build()
                );
            }
            OemAccount oemAccount = existing.get();
            String oemParentId = Objects.nonNull(oemAccount.getParent()) ? oemAccount.getParent().getId() : oemAccount.getId();
            validateDuplicateName(oemParentId, null, teamUpsertDto.getName());

            OemTeam newOemTeam = TEAM_MAPPER.mapToOemTeam(teamUpsertDto);
            newOemTeam.setOemGroup(oemAccount.getOemGroup());
            newOemTeam.setOemParent(Objects.nonNull(oemAccount.getParent()) ? oemAccount.getParent() : oemAccount);
            updateOemStaff(teamUpsertDto.getStaffIds(), newOemTeam);
            oemTeamRepository.saveAndFlush(newOemTeam);
        }
    }

    @Transactional
    public void editTeam(String id, TeamUpsertDto teamUpsertDto) {
        // TODO: Cleanup — remove postcode, prefecture_id, city_id, tel, fax_code, email columns since they are no longer in the DTO.
        Account account = GeneralUtils.getCurrentUser();
        if (Objects.equals(account.getRole(), Role.OPERATOR.getId())) {
            validateDuplicateName(null, id, teamUpsertDto.getName());

            OperatorTeam operatorTeam = getExistingOperatorTeamById(id);
            TEAM_MAPPER.updateOperatorTeam(operatorTeam, teamUpsertDto);
            updateOperatorStaff(teamUpsertDto.getStaffIds(), operatorTeam);
            operatorTeamRepository.saveAndFlush(operatorTeam);
        } else {
            OemTeam oemTeam = getExistingOemTeamById(id);
            validateDuplicateName(oemTeam.getOemParent().getId(), id, teamUpsertDto.getName());

            TEAM_MAPPER.updateOemTeam(oemTeam, teamUpsertDto);
            updateOemStaff(teamUpsertDto.getStaffIds(), oemTeam);
            oemTeamRepository.saveAndFlush(oemTeam);
        }
    }

    public TeamDetailsDto getTeamDetails(String id) {
        Account account = GeneralUtils.getCurrentUser();

        switch(Role.fromId(account.getRole())) {
            case Role.OPERATOR -> {
                OperatorTeam operatorTeam = getExistingOperatorTeamById(id);
                List<OperatorAccount> staffIds = operatorAccountRepository.findAllByTeam(id);
                TeamDetailsDto teamDetails = TEAM_MAPPER.toOperatorTeamDetailsDto(operatorTeam);
                teamDetails.setStaffIds(staffIds.stream().map(OperatorAccount::getId).toList());
                return teamDetails;
            }
            case Role.OEM -> {
                OemTeam oemTeam = getExistingOemTeamById(id);
                List<OemAccount> staffIds = oemAccountRepository.findAllByTeam(utilService.getParentIdAndGroupId(account).getLeft(), id);
                TeamDetailsDto teamDetails = TEAM_MAPPER.toOemTeamDetailsDto(oemTeam);
                teamDetails.setStaffIds(staffIds.stream().map(OemAccount::getId).toList());
                return teamDetails;
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public TeamListDto getTeams(Integer pageNumber, Integer pageSize, String searchInput) {
        Account account = GeneralUtils.getCurrentUser();

        switch(Role.fromId(account.getRole())) {
            case Role.OPERATOR -> {
                Page<OperatorTeam> teamList = operatorTeamRepository.findAllBy(
                        GeneralUtils.wrapToLike(searchInput),
                        GeneralUtils.getPagination(pageNumber, pageSize)
                );
                return TeamListDto.builder()
                        .total(teamList.getTotalElements())
                        .limit(teamList.getSize())
                        .page(teamList.getNumber() + 1)
                        .items(teamList.getContent().stream().map(TEAM_MAPPER::toOperatorTeamItemsDto)
                                .collect(Collectors.toList()))
                        .build();
            }
            case Role.OEM -> {
                Page<OemTeam> teamList = oemTeamRepository.findAllBy(
                        utilService.getParentIdAndGroupId(account).getLeft(),
                        GeneralUtils.wrapToLike(searchInput),
                        GeneralUtils.getPagination(pageNumber, pageSize)
                );
                return TeamListDto.builder()
                        .total(teamList.getTotalElements())
                        .limit(teamList.getSize())
                        .page(teamList.getNumber() + 1)
                        .items(teamList.getContent().stream().map(TEAM_MAPPER::toOemTeamItemsDto)
                                .collect(Collectors.toList()))
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public MDropdownListDto getOemTeams(Integer pageNumber, Integer pageSize, String id) {
        Account account = GeneralUtils.getCurrentUser();
        Page<OemTeam> teams;

        if (Objects.equals(account.getRole(), Role.OPERATOR.getId())) {
            if (Objects.isNull(id)) {
                throw new BadValidationException(
                        ErrorResponse.builder()
                                .statusCode(ErrorMessage.REQUIRED_FIELD.getStatusCode())
                                .message(String.format(ErrorMessage.REQUIRED_FIELD.getMessage(),
                                        TargetName.BRANCH.getTargetName()))
                                .fieldError("id")
                                .build()
                );
            }
            OemAccount oemAccount = getExistingById(id);

            teams = oemTeamRepository.findAllByOemId(
                    oemAccount.getOemGroup().getId(),
                    Objects.nonNull(oemAccount.getParent()) ? oemAccount.getParent().getId() : oemAccount.getId(),
                    GeneralUtils.getPagination(pageNumber, pageSize)
            );
        } else {
            OemAccount oemAccount = getExistingById(account.getId());

            teams = oemTeamRepository.findAllByOemId(
                    oemAccount.getOemGroup().getId(),
                    Objects.nonNull(oemAccount.getParent()) ? oemAccount.getParent().getId() : oemAccount.getId(),
                    GeneralUtils.getPagination(pageNumber, pageSize)
            );
        }

        return MDropdownListDto.builder()
                .total(teams.getTotalElements())
                .limit(teams.getSize())
                .page(teams.getNumber() + 1)
                .items(teams.getContent().stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    public MDropdownListDto getOperatorTeams(Integer pageNumber, Integer pageSize) {
        Page<OperatorTeam> teams = operatorTeamRepository.findAll(GeneralUtils.getPagination(pageNumber, pageSize));
        return MDropdownListDto.builder()
                .total(teams.getTotalElements())
                .limit(teams.getSize())
                .page(teams.getNumber() + 1)
                .items(teams.getContent().stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    private void validateDuplicateName(String oemParentId, String id, String name) {
        if (Objects.isNull(oemParentId)) {
            Optional<OperatorTeam> duplicate = operatorTeamRepository.findDuplicateBy(name, id);
            if (duplicate.isPresent()) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.DUPLICATE_DATA.getStatusCode())
                        .message(String.format(ErrorMessage.DUPLICATE_DATA.getMessage(),
                                TargetName.TEAM.getTargetName(),
                                name))
                        .build());
            }
        } else {
            Optional<OemTeam> duplicate = oemTeamRepository.findDuplicateBy(oemParentId, name, id);
            if (duplicate.isPresent()) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.DUPLICATE_DATA.getStatusCode())
                        .message(String.format(ErrorMessage.DUPLICATE_DATA.getMessage(),
                                TargetName.TEAM.getTargetName(),
                                name))
                        .build());
            }
        }
    }

    private void updateOperatorStaff(List<String> staffIds, OperatorTeam team) {
        if (Objects.isNull(staffIds) || staffIds.isEmpty()) {
            team.setStaffs(null);
        } else {
            List<OperatorAccount> staffs = getExistingOperatorStaffByIds(staffIds);
            team.setStaffs(staffs);
        }
    }

    private void updateOemStaff(List<String> staffIds, OemTeam team) {
        if (Objects.isNull(staffIds) || staffIds.isEmpty()) {
            team.setStaffs(null);
        } else {
            List<OemAccount> staffs = getExistingOemStaffByIds(staffIds);
            team.setStaffs(staffs);
        }
    }

    @Transactional
    public void deleteSelectedTeams(SelectedIds selectedIds) {
        Account account = GeneralUtils.getCurrentUser();
        switch (Role.fromId(account.getRole())) {
            case Role.OPERATOR -> {
                operatorTeamRepository.deleteOpAcountTeamsByParentAndIdIn(selectedIds.getSelectedIds());
                operatorTeamRepository.deleteTeamsByParentAndIdIn(selectedIds.getSelectedIds());
            }
            case OEM -> {
                oemTeamRepository.deleteOemAcountTeamsByParentAndIdIn(selectedIds.getSelectedIds());
                oemTeamRepository.deleteTeamsByParentAndIdIn(selectedIds.getSelectedIds());
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }
}
