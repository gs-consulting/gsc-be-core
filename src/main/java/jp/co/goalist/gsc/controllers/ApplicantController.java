package jp.co.goalist.gsc.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jp.co.goalist.gsc.gen.apis.ApplicantsApi;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.services.ApplicantService;
import jp.co.goalist.gsc.services.BulkExportService;
import jp.co.goalist.gsc.services.HistoryCallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ApplicantController implements ApplicantsApi {

    private final ApplicantService applicantService;
    private final HistoryCallService historyCallService;
    private final BulkExportService bulkExportService;
    private final HttpServletResponse response;

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApplicantStatusCountDto> countApplicationStatusNotChanged() {
        return ResponseEntity.ok(applicantService.countApplicationStatusNotChanged());
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApplicantStatusCountDto> countNewApplications() {
        return ResponseEntity.ok(applicantService.countNewApplications());
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> createNewApplicant(ApplicantUpsertDto applicantUpsertDto) {
        log.info("createNewApplicant");
        applicantService.createNewApplicant(applicantUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> deleteSelectedApplicants(SelectedIds selectedIds) {
        log.info("deleteSelectedApplicants, selectedIds {}", selectedIds.getSelectedIds());
        applicantService.deleteSelectedApplicants(selectedIds);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> editApplicant(String id, ApplicantUpsertDto applicantUpsertDto) {
        log.info("editApplicant, id {}", id);
        applicantService.editApplicant(id, applicantUpsertDto);
        return ResponseEntity.ok().build();

    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> editApplicantMemo(String id, ApplicantMemoDto applicantMemoDto) {
        log.info("editApplicantMemo, id {}", id);
        applicantService.editApplicantMemo(id, applicantMemoDto);
        return ResponseEntity.ok().build();

    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> editApplicantProject(String id, ApplicantProjectDto applicantProjectDto) {
        log.info("editApplicantProject, id {}", id);
        applicantService.editApplicantProject(id, applicantProjectDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> editApplicantStatus(String id, ApplicantStatusUpsertDto applicantStatusUpsertDto) {
        log.info("editApplicantStatus, id {}", id);
        applicantService.editApplicantSelectionStatus(id, applicantStatusUpsertDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApplicantDetailsDto> getApplicantDetails(String id) {
        log.info("getApplicantDetails, id {}", id);
        return ResponseEntity.ok(applicantService.getApplicantDetails(id));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApplicantListDto> getApplicants(ApplicantSearchDto applicantSearchDto) {
        log.info("getApplicants");
        return ResponseEntity.ok(applicantService.getApplicants(applicantSearchDto));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> createNewHistoryCall(String applicantId, HistoryCallUpsertDto historyCallUpsertDto) {
        log.info("createNewHistoryCall, applicantId {}", applicantId);
        historyCallService.createNewHistoryCall(applicantId, historyCallUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> deleteSelectedHistories(String applicantId, SelectedIds selectedIds) {
        log.info("deleteSelectedHistories, applicantId {}", applicantId);
        historyCallService.deleteSelectedHistories(applicantId, selectedIds);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> downloadApplicantCSV(ApplicantSearchDto applicantSearchDto) {
        log.info("downloadApplicantCSV");
        bulkExportService.downloadApplicantCSV(applicantSearchDto, response);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> editHistoryCallMemo(String applicantId, String id, HistoryCallMemoDto historyCallMemoDto) {
        log.info("editHistoryCallMemo, applicantId {}", applicantId);
        historyCallService.editHistoryCallMemo(applicantId, id, historyCallMemoDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<HistoryCallItemsDto>> getHistoryCalls(String applicantId) {
        log.info("getHistoryCalls, applicantId {}", applicantId);
        return ResponseEntity.ok(historyCallService.getHistoryCalls(applicantId));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> editHistoryCall(String applicantId, String id, HistoryCallUpsertDto historyCallUpsertDto) {
        log.info("editHistoryCall, applicantId {}", applicantId);
        historyCallService.editHistoryCall(applicantId, id, historyCallUpsertDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<HistoryCallDetailsDto> getHistoryCallDetails(String applicantId, String id) {
        log.info("getHistoryCallDetails, applicantId {}", applicantId);
        return ResponseEntity.ok(historyCallService.getHistoryCallDetails(applicantId, id));
    }
}
