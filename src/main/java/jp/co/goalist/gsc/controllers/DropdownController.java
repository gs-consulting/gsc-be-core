package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.gen.apis.DropdownsApi;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DropdownController implements DropdownsApi {

    private final DropdownService dropdownService;
    private final BranchService branchService;
    private final OperatorService operatorService;
    private final ClientAccountService clientAccountService;
    private final TeamService teamService;
    private final OemService oemService;
    private final StoreService storeService;

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM', 'CLIENT')")
    public ResponseEntity<MPostCodeDto> getAddressByPostCode(String postCode) {
        log.info("getAddressByPostCode, code: {}", postCode);
        return ResponseEntity.ok(dropdownService.getAddressByPostCode(postCode));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public ResponseEntity<MDropdownListDto> getClientAccountsDropdown(Boolean isInterviewer) {
        log.info("getClientAccountsDropdown, isInterviewer {}", isInterviewer);
        return ResponseEntity.ok(clientAccountService.getClientAccountsDropdown(isInterviewer));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public ResponseEntity<MDropdownListDto> getClientBranchesDropdown() {
        log.info("getClientBranchesDropdown");
        return ResponseEntity.ok(branchService.getClientBranchesDropdown());
    }

    @Override
    public ResponseEntity<MediaDropdownListDto> getClientMediaDropdown(String projectId, String applicantId) {
        log.info("getClientMediaDropdown, projectId {}", projectId);
        return ResponseEntity.ok(dropdownService.getClientMediaDropdown(projectId, applicantId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public ResponseEntity<MDropdownListDto> getClientStoresDropdown(List<String> branchIds) {
        log.info("getClientStoresDropdown");
        return ResponseEntity.ok(storeService.getClientStoresDropdown(branchIds));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM', 'CLIENT')")
    public ResponseEntity<List<MTPrefectureItemsDto>> getMasterPrefectures() {
        log.info("getMasterPrefectures");
        return ResponseEntity.ok(dropdownService.getMasterPrefectures());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OEM')")
    public ResponseEntity<MDropdownListDto> getOemBranches(Integer pageNumber, Integer pageSize, String clientAccountId, String storeId) {
        log.info("getOemBranches");
        return ResponseEntity.ok(branchService.getOemBranches(pageNumber, pageSize, clientAccountId, storeId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<MDropdownListDto> getOemClients(Integer pageNumber, Integer pageSize, String managerId, String branchId, String storeId) {
        log.info("getOemClients");
        return ResponseEntity.ok(clientAccountService.getOemClients(pageNumber, pageSize, managerId, branchId, storeId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OEM')")
    public ResponseEntity<MDropdownListDto> getOemManagers(Integer pageNumber, Integer pageSize, String teamId) {
        log.info("getOemManagers");
        return ResponseEntity.ok(oemService.getOemManagers(pageNumber, pageSize, teamId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<MDropdownListDto> getOemProjects(Integer pageNumber, Integer pageSize, String storeId) {
        log.info("getOemProjects");
        return ResponseEntity.ok(oemService.getOemProjects(pageNumber, pageSize, storeId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<MDropdownListDto> getOemStores(Integer pageNumber, Integer pageSize, List<String> branchIds, String clientAccountId, Integer isAll) {
        log.info("getOemStores");
        return ResponseEntity.ok(storeService.getOemStores(pageNumber, pageSize, branchIds, clientAccountId, isAll));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<MDropdownListDto> getOemTeams(Integer pageNumber, Integer pageSize, String oemId) {
        log.info("getOemTeams, oemId {}", oemId);
        return ResponseEntity.ok(teamService.getOemTeams(pageNumber, pageSize, oemId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OEM')")
    public ResponseEntity<MDropdownListDto> getOemStaffs(Integer pageNumber, Integer pageSize) {
        log.info("getOemStaffs");
        return ResponseEntity.ok(oemService.getOemStaffs(pageNumber, pageSize));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<MDropdownListDto> getOperatorBranches(Integer pageNumber, Integer pageSize, String clientAccountId, String storeId) {
        log.info("getOperatorBranches");
        return ResponseEntity.ok(branchService.getOperatorBranches(pageNumber, pageSize, clientAccountId, storeId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<MDropdownListDto> getOperatorClients(Integer pageNumber, Integer pageSize, String managerId, String branchId, String storeId) {
        log.info("getOperatorClients");
        return ResponseEntity.ok(clientAccountService.getOperatorClients(pageNumber, pageSize, managerId, branchId, storeId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    public ResponseEntity<MDropdownListDto> getOperatorManagers(Integer pageNumber, Integer pageSize, String teamId) {
        log.info("getOperatorManagers");
        return ResponseEntity.ok(operatorService.getOperatorManagers(pageNumber, pageSize, teamId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    public ResponseEntity<MDropdownListDto> getOperatorProjects(Integer pageNumber, Integer pageSize, String storeId) {
        log.info("getOperatorProjects");
        return ResponseEntity.ok(operatorService.getOperatorProjects(pageNumber, pageSize, storeId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<MDropdownListDto> getOperatorStores(Integer pageNumber, Integer pageSize, List<String> branchIds, String clientAccountId, Integer isAll) {
        log.info("getOperatorStores");
        return ResponseEntity.ok(storeService.getOperatorStores(pageNumber, pageSize, branchIds, clientAccountId, isAll));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    public ResponseEntity<MDropdownListDto> getOperatorTeams(Integer pageNumber, Integer pageSize) {
        log.info("getOperatorTeams");
        return ResponseEntity.ok(teamService.getOperatorTeams(pageNumber, pageSize));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    public ResponseEntity<MDropdownListDto> getOperatorStaffs(Integer pageNumber, Integer pageSize) {
        log.info("getOperatorStaffs");
        return ResponseEntity.ok(operatorService.getOperatorStaffs(pageNumber, pageSize));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<MDropdownListDto> getClientStatusDropdown(String statusId) {
        log.info("getClientStatusDropdown, statusId: {}", statusId);
        return ResponseEntity.ok(dropdownService.getClientStatusDropdown(statusId));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<MDropdownListDto> getClientProjectsDropdown() {
        log.info("getClientProjectsDropdown");
        return ResponseEntity.ok(dropdownService.getClientProjectsDropdown());
    }

    @Override
    public ResponseEntity<MDropdownListDto> getClientApplicantColumnsDropdown() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getClientApplicantColumnsDropdown'");
    }
}
