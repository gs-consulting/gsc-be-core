package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.gen.apis.ClientsApi;
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
@RestController
@RequiredArgsConstructor
public class ClientController implements ClientsApi {

    private final ClientSettingService clientSettingService;
    private final ClientAccountService clientAccountService;
    private final BranchService branchService;
    private final StoreService storeService;
    private final ProjectService projectService;
    private final SurveyService surveyService;
    private final SurveyStatisticService surveyStatisticService;
    private final TemplateService templateService;

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> createClientBranch(ClientBranchUpsertDto clientBranchUpsertDto) {
        log.info("createClientBranch");
        branchService.createClientBranch(clientBranchUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> createClientStore(ClientStoreUpsertDto clientStoreUpsertDto) {
        log.info("createClientStore");
        storeService.createClientStore(clientStoreUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> createClientUserAccount(ClientUserAccountUpsertDto clientUserAccountUpsertDto) {
        log.info("createClientUserAccount");
        clientAccountService.createClientUserAccount(clientUserAccountUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> createMasterMedia(MasterMediaUpsertDto masterMediaUpsertDto) {
        log.info("upsertMasterMedia");
        clientSettingService.createMasterMedia(masterMediaUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> upsertMediaReportSettings(
            MasterMediaReportUpsertDto masterMediaReportUpsertDto) {
        log.info("upsertMediaReportSettings");
        clientSettingService.upsertMediaReportSettings(masterMediaReportUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> upsertMonthlyCost(String selectedDate,
                                                  List<MasterMonthlyCostUpsertDto> masterMonthlyCostUpsertDto) {
        log.info("upsertMonthlyCost: {}", selectedDate);
        clientSettingService.upsertMonthlyCost(selectedDate, masterMonthlyCostUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> upsertMasterDataStatuses(
            String statusType,
            List<MasterDataStatusUpsertDto> masterDataStatusUpsertDto) {
        log.info("upsertMasterDataStatuses");
        clientSettingService.upsertMasterDataStatuses(statusType, masterDataStatusUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> upsertSelectionStatuses(
            List<MasterSelectionStatusUpsertDto> masterSelectionStatusUpsertDto) {
        log.info("upsertSelectionStatuses");
        clientSettingService.upsertSelectionStatuses(masterSelectionStatusUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> createNewSurvey(SurveyUpsertDto surveyUpsertDto) {
        log.info("createSurvey");
        surveyService.createNewSurvey(surveyUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> createTemplate(TemplateUpsertDto templateUpsertDto) {
        log.info("createTemplate");
        templateService.createTemplate(templateUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> deleteSelectedSurveys(SelectedIds selectedIds) {
        log.info("deleteSelectedSurveys");
        surveyService.deleteSelectedSurveys(selectedIds);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> deleteSelectedTemplates(SelectedIds selectedIds) {
        log.info("deleteSelectedTemplates");
        templateService.deleteSelectedTemplates(selectedIds);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> editClientBranch(String id, ClientBranchUpsertDto clientBranchUpsertDto) {
        log.info("editClientBranch, id {}", id);
        branchService.editClientBranch(id, clientBranchUpsertDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> editClientStore(String id, ClientStoreUpsertDto clientStoreUpsertDto) {
        log.info("editClientStore, id {}", id);
        storeService.editClientStore(id, clientStoreUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> editClientUserAccount(String accountId,
                                                      ClientUserAccountUpsertDto clientUserAccountUpsertDto) {
        log.info("editClientUserAccount, id {}", accountId);
        clientAccountService.editUserClientAccount(accountId, clientUserAccountUpsertDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> editMasterMedia(String id, MasterMediaUpsertDto masterMediaUpsertDto) {
        log.info("editMasterMedia, id {}", id);
        clientSettingService.editMasterMedia(id, masterMediaUpsertDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> editSurvey(String id, SurveyUpsertDto surveyUpsertDto) {
        log.info("editSurvey, id {}", id);
        surveyService.editSurvey(id, surveyUpsertDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> editTemplate(String id, TemplateUpsertDto templateUpsertDto) {
        log.info("editTemplate, id {}", id);
        templateService.editTemplate(id, templateUpsertDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ClientBranchDetailsDto> getClientBranchDetails(String id) {
        log.info("getClientBranchDetails, id {}", id);
        return ResponseEntity.ok(branchService.getClientBranchDetails(id));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<ClientBranchDataItemsDto>> getClientBranchRelatedData() {
        log.info("getClientBranchRelatedData");
        return ResponseEntity.ok(projectService.getClientBranchRelatedData());
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ClientBranchListDto> getClientBranches(Integer pageNumber, Integer pageSize,
                                                                 String searchInput) {
        log.info("getClientBranches");
        return ResponseEntity.ok(branchService.getClientBranches(pageNumber, pageSize, searchInput));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ClientStoreDetailsDto> getClientStoreDetails(String id) {
        log.info("getClientStoreDetails, id {}", id);
        return ResponseEntity.ok(storeService.getClientStoreDetails(id));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ClientStoreListDto> getClientStores(String branchId, Integer pageNumber, Integer pageSize, String searchInput) {
        log.info("getClientStores, branchId {}", branchId);
        return ResponseEntity.ok(storeService.getClientStores(branchId, searchInput, pageNumber, pageSize));
    }
    
    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ClientUserAccountDetailsDto> getClientUserAccountDetails(String accountId) {
        log.info("getClientUserAccountDetails, id {}", accountId);
        return ResponseEntity.ok(clientAccountService.getUserClientAccountDetails(accountId));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ClientUserAccountListDto> getClientUserAccounts(Integer pageNumber, Integer pageSize,
                                                                          String searchInput) {
        log.info("getClientUserAccounts");
        return ResponseEntity.ok(clientAccountService.getUserClientAccounts(pageNumber, pageSize, searchInput));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<MasterMediaDetailsDto> getMasterMediaDetails(String id) {
        log.info("getMasterMediaDetails: {}", id);
        return ResponseEntity.ok(clientSettingService.getMasterMediaDetails(id));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<MasterMediaListDto> getMasterMedias(Integer pageNumber, Integer pageSize,
                                                              String searchInput) {
        log.info("getMasterMedias: {}", searchInput);
        return ResponseEntity.ok(clientSettingService.getMasterMedias(pageNumber, pageSize, searchInput));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<MasterMediaReportListDto> getMediaReportSettings() {
        log.info("getMediaReportSettings");
        return ResponseEntity.ok(clientSettingService.getMediaReportSettings());
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<MasterMonthlyCostItemsDto>> getMonthlyCost(String selectedDate) {
        log.info("getMonthlyCost");
        return ResponseEntity.ok(clientSettingService.getMonthlyCost(selectedDate));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<MasterDataStatusItemsDto>> getMasterDataStatuses(String statusType) {
        log.info("getMasterDataStatuses");
        return ResponseEntity.ok(clientSettingService.getMasterDataStatuses(statusType));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<MasterSelectionStatusItemsDto>> getSelectionStatuses() {
        log.info("getSelectionStatuses");
        return ResponseEntity.ok(clientSettingService.getSelectionStatuses());
    }

    @Override
    public ResponseEntity<SurveyDetailsDto> getSurveyDetails(String id) {
        log.info("getSurveyDetails, id {}", id);
        return ResponseEntity.ok(surveyService.getSurveyDetails(id));
    }

    @Override
    public ResponseEntity<SurveyListDto> getSurveyList(SurveySearchDto surveySearchDto) {
        log.info("getSurveyList");
        return ResponseEntity.ok(surveyService.getSurveyList(surveySearchDto));
    }

    @Override
    public ResponseEntity<TemplateDetailsDto> getTemplateDetails(String id) {
        log.info("getTemplateDetails, id {}", id);
        return ResponseEntity.ok(templateService.getTemplateDetails(id));
    }

    @Override
    public ResponseEntity<TemplateListDto> getTemplateList(TemplateSearchDto templateSearchDto) {
        log.info("getTemplateList");
        return ResponseEntity.ok(templateService.getTemplateList(templateSearchDto));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> upsertInterviewCategories(
            List<MasterInterviewCategoryUpsertDto> masterInterviewCategoryUpsertDto) {
        log.info("upsertInterviewCategories");
        clientSettingService.upsertInterviewCategories(masterInterviewCategoryUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<MasterInterviewCategoryItemsDto>> getInterviewCategories() {
        log.info("getInterviewCategories");
        return ResponseEntity.ok(clientSettingService.getInterviewCategories());
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SurveyLinkDto> getSurveyPublicLink(SurveyLinkRequestDto surveyLinkRequestDto) {
        log.info("getSurveyPublicLink");
        return ResponseEntity.ok(surveyService.getSurveyPublicLink(surveyLinkRequestDto));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SurveyAnswersListDto> getSurveyStatisticAnswers(String surveyId, SurveyStatisticSearchDto surveyStatisticSearchDto) {
        log.info("getSurveyStatisticAnswers");
        return ResponseEntity.ok(surveyStatisticService.getSurveyStatisticAnswers(surveyId, surveyStatisticSearchDto));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SurveyStatisticDto> getSurveyStatisticsSummary(String surveyId) {
        log.info("getSurveyStatisticsSummary");
        return ResponseEntity.ok(surveyStatisticService.getSurveyStatisticsSummary(surveyId));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> deleteSelectedBranches(SelectedIds selectedIds) {
        log.info("deleteSelectedBranches");
        branchService.deleteSelectedBranches(selectedIds);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> deleteSelectedStores(SelectedIds selectedIds) {
        log.info("deleteSelectedStores");
        storeService.deleteSelectedStores(selectedIds);
        return ResponseEntity.noContent().build();
    }
}
