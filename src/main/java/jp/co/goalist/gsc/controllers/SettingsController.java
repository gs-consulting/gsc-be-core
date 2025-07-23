package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.gen.apis.SettingsApi;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SettingsController implements SettingsApi {

    private final TeamService teamService;
    private final OperatorService operatorService;
    private final OemService oemService;
    private final ClientAccountService clientAccountService;
    private final ClientService clientService;

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<Void> createClientAccount(ClientAccountUpsertDto clientAccountUpsertDto) {
        log.info("createClientAccount");
        clientAccountService.createClientAccount(clientAccountUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<Void> createNewOemGroup(OemUpsertDto oemUpsertDto) {
        log.info("createNewOemGroup, {}", oemUpsertDto.getOemName());
        clientService.createNewOemGroup(oemUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<Void> createOemAccount(String id, OemAccountUpsertDto oemAccountUpsertDto) {
        log.info("createOemAccount, id: {}", id);
        oemService.createOemAccount(id, oemAccountUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('OEM')")
    public ResponseEntity<Void> createStaffOemAccount(StaffOemAccountUpsertDto staffOemAccountUpsertDto) {
        log.info("createStaffOemAccount");
        oemService.createStaffOemAccount(staffOemAccountUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<Void> createStaffOperatorAccount(StaffOperatorAccountUpserDto staffOperatorAccountUpserDto) {
        log.info("createStaffOperatorAccount");
        operatorService.createStaffOperatorAccount(staffOperatorAccountUpserDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<Void> createTeam(TeamUpsertDto teamUpsertDto) {
        teamService.createTeam(teamUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<Void> editClientAccount(String accountId, ClientAccountUpsertDto clientAccountUpsertDto) {
        log.info("editClientAccount, id: {}", accountId);
        clientAccountService.editClientAccount(accountId, clientAccountUpsertDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<Void> editNewOemGroup(String id, OemUpsertDto oemUpsertDto) {
        log.info("editNewOemGroup, id: {}", id);
        clientService.editNewOemGroup(id, oemUpsertDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<Void> editOemAccount(String id, String accountId, OemAccountUpsertDto oemAccountUpsertDto) {
        oemService.editOemAccount(id, accountId, oemAccountUpsertDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('OEM')")
    public ResponseEntity<Void> editStaffOemAccount(String accountId, StaffOemAccountUpsertDto staffOemAccountUpsertDto) {
        log.info("editStaffOemAccount, id: {}", accountId);
        oemService.editStaffOemAccount(accountId, staffOemAccountUpsertDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<Void> editStaffOperatorAccount(String accountId, StaffOperatorAccountUpserDto staffOperatorAccountUpserDto) {
        log.info("editStaffOperatorAccount, id: {}", accountId);
        operatorService.editStaffOperatorAccount(accountId, staffOperatorAccountUpserDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<Void> editTeam(String id, TeamUpsertDto teamUpsertDto) {
        log.info("editTeam, id {}", id);
        teamService.editTeam(id, teamUpsertDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<ClientAccountDetailsDto> getClientAccountDetails(String accountId) {
        log.info("getClientAccountDetails, id {}", accountId);
        return ResponseEntity.ok(clientAccountService.getClientAccountDetails(accountId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<ClientAccountListDto> getClientAccounts(Integer pageNumber, Integer pageSize, String searchInput) {
        log.info("getClientAccounts");
        return ResponseEntity.ok(clientAccountService.getClientAccounts(pageNumber, pageSize, searchInput));
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<OemAccountDetailsDto> getOemAccountDetails(String id, String accountId) {
        log.info("getOemAccountDetails, id {}", id);
        return ResponseEntity.ok(oemService.getOemAccountDetails(id, accountId));
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<OemAccountListDto> getOemAccounts(String id, Integer pageNumber, Integer pageSize, String searchInput) {
        log.info("getOemAccounts, oemGroupId: {}", id);
        return ResponseEntity.ok(oemService.getOemAccounts(id, pageNumber, pageSize, searchInput));
    }

    @Override
    public ResponseEntity<List<OemGroupItemsDto>> getOemGroups(Boolean isClientEdit) {
        log.info("getOemGroups, isClientEdit {}", isClientEdit);
        return ResponseEntity.ok(clientService.getOemGroups(isClientEdit));
    }

    @Override
    @PreAuthorize("hasAuthority('OEM')")
    public ResponseEntity<StaffOemAccountDetailsDto> getStaffOemAccountDetails(String accountId) {
        log.info("getStaffOemAccountDetails, id: {}", accountId);
        return ResponseEntity.ok(oemService.getStaffOemAccountDetails(accountId));
    }

    @Override
    @PreAuthorize("hasAuthority('OEM')")
    public ResponseEntity<StaffOemAccountListDto> getStaffOemAccounts(Integer pageNumber, Integer pageSize, String searchInput) {
        log.info("getStaffOemAccounts");
        return ResponseEntity.ok(oemService.getStaffOemAccounts(pageNumber, pageSize, searchInput));
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<StaffOperatorAccountDetailsDto> getStaffOperatorAccountDetails(String accountId) {
        log.info("getStaffOperatorAccountDetails, id {}", accountId);
        return ResponseEntity.ok(operatorService.getStaffOperatorAccountDetails(accountId));
    }

    @Override
    public ResponseEntity<StaffOperatorAccountListDto> getStaffOperatorAccounts(Integer pageNumber, Integer pageSize, String searchInput) {
        log.info("getStaffOperatorAccounts");
        return ResponseEntity.ok(operatorService.getStaffOperatorAccounts(pageNumber, pageSize, searchInput));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<TeamDetailsDto> getTeamDetails(String id) {
        log.info("getTeamDetails, id {}", id);
        return ResponseEntity.ok(teamService.getTeamDetails(id));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<TeamListDto> getTeams(Integer pageNumber, Integer pageSize, String searchInput) {
        log.info("getTeams");
        return ResponseEntity.ok(teamService.getTeams(pageNumber, pageSize, searchInput));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<Void> deleteSelectedTeams(SelectedIds selectedIds) {
        log.info("deleteSelectedSurveys");
        teamService.deleteSelectedTeams(selectedIds);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<Void> deleteSelectedOperatorStaffs(SelectedIds selectedIds) {
        log.info("deleteSelectedOperatorStaffs");
        operatorService.deleteSelectedOperatorStaffs(selectedIds);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('OEM')")
    public ResponseEntity<Void> deleteSelectedOemStaffs(SelectedIds selectedIds) {
        log.info("deleteSelectedOemStaffs");
        oemService.deleteSelectedOemStaffs(selectedIds);
        return ResponseEntity.ok().build();
    }
}