package jp.co.goalist.gsc.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jp.co.goalist.gsc.gen.apis.ProjectsApi;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.services.BulkExportService;
import jp.co.goalist.gsc.services.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProjectController implements ProjectsApi {

    private final ProjectService projectService;
    private final BulkExportService bulkExportService;
    private final HttpServletResponse response;

    @Override
    public ResponseEntity<Void> downloadProjectCSV(ProjectSearchBoxDto projectSearchBoxDto) {
        log.info("downloadProjectCSV");
        bulkExportService.downloadProjectCSV(projectSearchBoxDto, response);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ProjectApplicantsDto> getApplicationListBasedOnFlowType(String id, String flowType, Integer pageNumber, Integer pageSize, String arrangedBy) {
        log.info("getProjectAdvertisements, id {}, flowType {}", id, flowType);
        return ResponseEntity.ok(projectService.getApplicationListBasedOnFlowType(id, flowType, pageNumber, pageSize, arrangedBy));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ProjectAdvertListDto> getProjectAdvertisements(String id) {
        log.info("getProjectAdvertisements, id {}", id);
        return ResponseEntity.ok(projectService.getProjectAdvertisements(id));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ProjectListDto> getProjectList(ProjectSearchBoxDto projectSearchBoxDto) {
        log.info("getProjectList");
        return ResponseEntity.ok(projectService.getProjectList(projectSearchBoxDto));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<ProjectPermissionItemsDto>> getProjectListForBranchPermission(String id, ProjectSearchBoxDto projectSearchBoxDto) {
        log.info("getProjectListForBranchPermission, branchId {}", id);
        return ResponseEntity.ok(projectService.getProjectListForBranchPermission(id, projectSearchBoxDto));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> saveBranchPermissionSetting(String id, ProjectPermissionUpsertDto projectPermissionUpsertDto) {
        log.info("saveBranchPermissionSetting, id {}", id);
        projectService.saveProjectBranchPermissionSetting(id, projectPermissionUpsertDto);
        return ResponseEntity.ok().build();
    }

}
