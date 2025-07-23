package jp.co.goalist.gsc.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.gen.apis.PublicApi;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.gen.dtos.SurveyDetailsDto;
import jp.co.goalist.gsc.gen.dtos.SurveySubmissionDto;
import jp.co.goalist.gsc.services.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicController implements PublicApi {

    private final SurveyService surveyService;

    private HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttr == null) {
            throw new NullPointerException("リクエスト属性がnullです");
        }
        return requestAttr.getRequest();
    }

    @Override
    public ResponseEntity<Void> submitSurvey(SurveySubmissionDto surveySubmissionDto) {
        log.info("submitSurvey");
        HttpServletRequest request = getHttpServletRequest();
        String authToken = request.getHeader("Authorization");
        if (authToken != null) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_OPERATOR.getStatusCode())
                    .message(ErrorMessage.INVALID_OPERATOR.getMessage())
                    .build());
        }
        
        surveyService.submitSurvey(surveySubmissionDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<SurveyDetailsDto> getQuestionsForPublicLink(String token) {
        log.info("getQuestionsForPublicLink");

        if (token == null || token.isEmpty()) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.REQUIRED_FIELD.getStatusCode())
                    .message(String.format(ErrorMessage.REQUIRED_FIELD.getMessage(), "token"))
                    .fieldError("token")
                    .build());
        }

        return ResponseEntity.ok(surveyService.getQuestionsForPublicLink(token));
    }

}
