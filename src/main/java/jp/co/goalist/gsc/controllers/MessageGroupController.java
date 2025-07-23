package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.gen.apis.MessageGroupsApi;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.services.ApplicantService;
import jp.co.goalist.gsc.services.SurveyService;
import jp.co.goalist.gsc.services.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MessageGroupController implements MessageGroupsApi {

    private final ApplicantService applicantService;
    private final SurveyService surveyService;
    private final TemplateService templateService;

    @Override
    public ResponseEntity<MProfileDto> getApplicantProfiles(String id) {
        log.info("getApplicantProfiles, id {}", id);
        return ResponseEntity.ok(applicantService.getApplicantProfiles(id));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<MTemplateListDto> getTemplatesForChat(TemplateSearchDto templateSearchDto) {
        log.info("getTemplatesForChat");
        return ResponseEntity.ok(templateService.getTemplatesForChat(templateSearchDto));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<MSurveyListDto> getSurveysForChat(SurveySearchDto surveySearchDto) {
        log.info("getSurveysForChat");
        return ResponseEntity.ok(surveyService.getSurveysForChat(surveySearchDto));
    }
}
