package jp.co.goalist.gsc.services;

import io.jsonwebtoken.lang.Strings;
import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.QuestionType;
import jp.co.goalist.gsc.enums.SubRole;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.*;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static jp.co.goalist.gsc.mappers.SurveyMapper.SURVEY_MAPPER;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final Map<String, String> SURVEY_SORT_MAP = new HashMap<>() {
        {
            put("createdAt", "createdAt");
            put("updatedAt", "updatedAt");
            put("name", "surveyName");
        }
    };
    private final Map<String, String> SORT_ORDERS = new HashMap<>() {
        {
            put("asc", Constants.SQL_ASC);
            put("desc", Constants.SQL_DESC);
        }
    };
    private final OperatorSurveyRepository operatorSurveyRepo;
    private final OemSurveyRepository oemSurveyRepo;
    private final OperatorSurveyAnswerRepository operatorSurveyAnswerRepo;
    private final OemSurveyAnswerRepository oemSurveyAnswerRepo;
    private final OperatorSurveyQuestionRepository operatorSurveyQuestionRepo;
    private final OemSurveyQuestionRepository oemSurveyQuestionRepo;
    private final OperatorApplicantRepository operatorApplicantRepo;
    private final OemApplicantRepository oemApplicantRepo;
    private final OperatorApplicantSurveyRepository operatorApplicantSurveyRepo;
    private final OemApplicantSurveyRepository oemApplicantSurveyRepo;
    private final OperatorApplicantAnswerRepository operatorApplicantAnswerRepo;
    private final OemApplicantAnswerRepository oemApplicantAnswerRepo;
    private final UtilService utilService;
    @Value("${gsc-be-core.frontend-url}")
    private String frontEndUrl;

    @Transactional
    public void createNewSurvey(SurveyUpsertDto surveyUpsertDto) {
        Account currentAccount = GeneralUtils.getCurrentUser();
        switch (SubRole.fromId(currentAccount.getSubRole())) {
            case OPERATOR -> {
                handleCreateOperatorSurvey(surveyUpsertDto, currentAccount);
            }
            case OEM -> {
                handleCreateOemSurvey(surveyUpsertDto, currentAccount);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public SurveyListDto getSurveyList(SurveySearchDto surveySearchDto) {
        Pageable pageable = GeneralUtils.getPagination(
                surveySearchDto.getPageNumber(),
                surveySearchDto.getPageSize(),
                getSortBy(surveySearchDto.getArrangedBy()));

        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                Page<OperatorSurvey> operatorSurveys = operatorSurveyRepo.findAllByParentId(
                        parentInfo.getLeft(),
                        GeneralUtils.wrapToLike(surveySearchDto.getSearchInput()),
                        pageable);

                return SurveyListDto.builder()
                        .items(operatorSurveys.getContent().stream()
                                .map(SURVEY_MAPPER::toOperatorSurveyItemsDto)
                                .toList())
                        .total(operatorSurveys.getTotalElements())
                        .page(operatorSurveys.getNumber() + 1)
                        .limit(operatorSurveys.getSize())
                        .build();
            }
            case OEM -> {
                Page<OemSurvey> oemSurveys = oemSurveyRepo.findAllByParentIdAndOemGroupId(
                        parentInfo.getLeft(),
                        parentInfo.getRight(),
                        GeneralUtils.wrapToLike(surveySearchDto.getSearchInput()),
                        pageable);

                return SurveyListDto.builder()
                        .items(oemSurveys.getContent().stream()
                                .map(SURVEY_MAPPER::toOemSurveyItemsDto)
                                .toList())
                        .total(oemSurveys.getTotalElements())
                        .page(oemSurveys.getNumber() + 1)
                        .limit(oemSurveys.getSize())
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public SurveyDetailsDto getSurveyDetails(String id) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                return getOperatorQAs(id, parentInfo.getLeft());
            }
            case OEM -> {
                return getOemQAs(id, parentInfo.getLeft(), parentInfo.getRight());
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void editSurvey(String id, SurveyUpsertDto surveyUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                OperatorSurvey survey = getExistingOperatorSurveyById(id, parentInfo.getLeft());
                // can not edit the survey if the survey is sent or completed by applicant
                if (!survey.getIsDeletable()) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_OPERATOR.getStatusCode())
                            .message(ErrorMessage.INVALID_OPERATOR.getMessage())
                            .fieldError("id")
                            .build());
                }
                // Update survey name
                survey.setSurveyName(surveyUpsertDto.getName());

                // Create set of question IDs from the submitted DTO for efficient lookup
                Set<Long> updatedQuestionIds = surveyUpsertDto.getContent().stream()
                        .filter(q -> q.getId() != null)
                        .map(q -> Long.parseLong(q.getId()))
                        .collect(Collectors.toSet());

                if (survey.getQuestions() != null) {
                    survey.getQuestions().removeIf(q -> !updatedQuestionIds.contains(q.getId()));
                }

                // Process questions and answers
                for (SurveyQuestionsUpsertDto questionDto : surveyUpsertDto.getContent()) {
                    processOperatorSurveyQuestion(survey, questionDto);
                }

                operatorSurveyRepo.save(survey);
            }
            case OEM -> {
                OemSurvey survey = getExistingOemSurveyById(id, parentInfo.getLeft(), parentInfo.getRight());
                // can not edit the survey if the survey is sent or completed by applicant
                if (!survey.getIsDeletable()) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_OPERATOR.getStatusCode())
                            .message(ErrorMessage.INVALID_OPERATOR.getMessage())
                            .fieldError("id")
                            .build());
                }
                // Update survey name
                survey.setSurveyName(surveyUpsertDto.getName());

                // Create set of question IDs from the submitted DTO for efficient lookup
                Set<Long> updatedQuestionIds = surveyUpsertDto.getContent().stream()
                        .filter(q -> q.getId() != null)
                        .map(q -> Long.parseLong(q.getId()))
                        .collect(Collectors.toSet());

                if (survey.getQuestions() != null) {
                    survey.getQuestions().removeIf(q -> !updatedQuestionIds.contains(q.getId()));
                }

                // Process questions and answers (important to uncomment this!)
                for (SurveyQuestionsUpsertDto questionDto : surveyUpsertDto.getContent()) {
                    processOemSurveyQuestion(survey, questionDto);
                }

                // Save the survey with updated questions
                oemSurveyRepo.save(survey);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    private void handleCreateOperatorSurvey(SurveyUpsertDto surveyUpsertDto, Account currentAccount) {
        OperatorSurvey survey = new OperatorSurvey();
        survey.setSurveyName(surveyUpsertDto.getName());

        List<OperatorSurveyQuestion> questions = surveyUpsertDto.getContent().stream()
                .map(question -> {
                    OperatorSurveyQuestion questionEnt = SURVEY_MAPPER.toOpSurveyQuestionEntity(question);
                    questionEnt.setSurvey(survey);

                    List<OperatorSurveyAnswer> answerEntList;
                    if (QuestionType.FREE_TEXT.getId().equals(questionEnt.getType())) {
                        // For FREE_TEXT questions, set an empty list as there are no predefined answers
                        answerEntList = new ArrayList<>();
                    } else {
                        // For other question types (SINGLE, MULTIPLE), process answers
                        answerEntList = question.getAnswers() != null ? question.getAnswers().stream()
                                .map(answer -> {
                                    OperatorSurveyAnswer answerEnt = SURVEY_MAPPER.toOpSurveyAnswerEntity(answer);
                                    answerEnt.setQuestion(questionEnt);
                                    // in case the answer is "other"
                                    if (answer.getAnswerText() == null || answer.getAnswerText().isEmpty()) {
                                        answerEnt.setAnswerText("");
                                        answerEnt.setIsFixed(false);
                                    } else {
                                        answerEnt.setIsFixed(true); // Not FREE_TEXT, so it's fixed
                                    }
                                    return answerEnt;
                                })
                                .toList() : new ArrayList<>();
                    }
                    questionEnt.setAnswers(answerEntList);
                    return questionEnt;
                })
                .toList();
        survey.setQuestions(questions);
        survey.setParent(utilService.getOperatorParent(currentAccount));
        operatorSurveyRepo.save(survey);
    }

    private void handleCreateOemSurvey(SurveyUpsertDto surveyUpsertDto, Account currentAccount) {
        OemSurvey survey = new OemSurvey();
        survey.setSurveyName(surveyUpsertDto.getName());

        List<OemSurveyQuestion> questions = surveyUpsertDto.getContent().stream()
                .map(question -> {
                    OemSurveyQuestion questionEnt = SURVEY_MAPPER.toOemSurveyQuestionEntity(question);
                    questionEnt.setSurvey(survey);

                    List<OemSurveyAnswer> answerEntList;
                    if (QuestionType.FREE_TEXT.getId().equals(questionEnt.getType())) {
                        // For FREE_TEXT questions, set an empty list as there are no predefined answers
                        answerEntList = new ArrayList<>();
                    } else {
                        // For other question types (SINGLE, MULTIPLE), process answers
                        answerEntList = question.getAnswers() != null ? question.getAnswers().stream()
                                .map(answer -> {
                                    OemSurveyAnswer answerEnt = SURVEY_MAPPER.toOemSurveyAnswerEntity(answer);
                                    answerEnt.setQuestion(questionEnt);
                                    // in case the answer is "other"
                                    if (answer.getAnswerText() == null || answer.getAnswerText().isEmpty()) {
                                        answerEnt.setAnswerText("");
                                        answerEnt.setIsFixed(false);
                                    } else {
                                        answerEnt.setIsFixed(true); // Not FREE_TEXT, so it's fixed
                                    }
                                    return answerEnt;
                                })
                                .toList() : new ArrayList<>();
                    }
                    questionEnt.setAnswers(answerEntList);
                    return questionEnt;
                })
                .toList();
        survey.setQuestions(questions);

        OemClientAccount parent = utilService.getOemParent(currentAccount);
        survey.setParent(parent);
        survey.setOemGroupId(parent.getOemGroupId());
        oemSurveyRepo.save(survey);
    }

    private Sort getSortBy(String arrangedBy) {
        if (arrangedBy == null) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] arrangedByArray = arrangedBy.split(":");
        if (arrangedByArray.length != 2 || !SURVEY_SORT_MAP.containsKey(arrangedByArray[0]) ||
                !SORT_ORDERS.containsKey(arrangedByArray[1])) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_OPERATOR.getStatusCode())
                    .message(ErrorMessage.INVALID_OPERATOR.getMessage())
                    .fieldError("arrangedBy")
                    .build());
        }

        return Sort.by(Sort.Direction.fromString(SORT_ORDERS.get(arrangedByArray[1])),
                SURVEY_SORT_MAP.get(arrangedByArray[0]));
    }

    private OperatorSurvey getExistingOperatorSurveyById(String id, String parentId) {
        return operatorSurveyRepo.findFetchById(id, parentId)
                .orElseThrow(() -> new NotFoundException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                        .message(ErrorMessage.NOT_FOUND.getMessage())
                        .fieldError("id")
                        .build()));
    }

    private OemSurvey getExistingOemSurveyById(String id, String parentId, String oemGroupId) {
        return oemSurveyRepo.findFetchById(id, parentId, oemGroupId)
                .orElseThrow(() -> new NotFoundException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                        .message(ErrorMessage.NOT_FOUND.getMessage())
                        .fieldError("id")
                        .build()));
    }

    private SurveyDetailsDto getOperatorQAs(String id, String parentId) {
        OperatorSurvey survey = getExistingOperatorSurveyById(id, parentId);
        SurveyDetailsDto surveyDetailsDto = SURVEY_MAPPER.toOpDetailsDto(survey);

        List<OperatorSurveyQuestion> questions = survey.getQuestions();
        surveyDetailsDto.setContent(getOpSurveyQuestionList(questions));

        return surveyDetailsDto;
    }

    private SurveyDetailsDto getOemQAs(String id, String parentId, String oemGroupId) {
        OemSurvey survey = getExistingOemSurveyById(id, parentId, oemGroupId);
        SurveyDetailsDto surveyDetailsDto = SURVEY_MAPPER.toOemDetailsDto(survey);

        List<OemSurveyQuestion> questions = survey.getQuestions();
        surveyDetailsDto.setContent(getOemSurveyQuestionList(questions));

        return surveyDetailsDto;
    }

    private void processOperatorSurveyQuestion(OperatorSurvey survey, SurveyQuestionsUpsertDto questionDto) {
        OperatorSurveyQuestion question;

        if (questionDto.getId() != null) {
            // Update existing question
            question = survey.getQuestions().stream()
                    .filter(q -> q.getId().equals(Long.parseLong(questionDto
                            .getId())))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                            .message(ErrorMessage.NOT_FOUND.getMessage())
                            .fieldError("questionId")
                            .build()));

            // Check if question type has changed
            String oldType = question.getType();
            SURVEY_MAPPER.updateOpSurveyQuestion(questionDto, question);

            // If type changed to FREE_TEXT, clear all answers
            if (QuestionType.FREE_TEXT.getId().equals(question.getType()) && !QuestionType.FREE_TEXT.getId().equals(oldType)) {
                if (question.getAnswers() != null) {
                    question.getAnswers().clear();
                }
                return;
            }
        } else {
            // Create new question
            question = SURVEY_MAPPER.toOpSurveyQuestionEntity(questionDto);
            question.setSurvey(survey);
            if (survey.getQuestions() == null) {
                survey.setQuestions(new ArrayList<>());
            }
            survey.getQuestions().add(question);
        }

        // Process answers only if not FREE_TEXT
        if (!QuestionType.FREE_TEXT.getId().equals(question.getType())) {
            processOperatorSurveyAnswers(question, questionDto.getAnswers());
        }
    }

    private void processOperatorSurveyAnswers(OperatorSurveyQuestion question, List<SurveyAnswerUpsertDto> answerDtos) {
        if (QuestionType.FREE_TEXT.getId().equals(question.getType()) ||
                answerDtos == null || answerDtos.isEmpty()) {
            // If question is FREE_TEXT, no need to process answers
            return;
        }

        if (question.getAnswers() == null) {
            question.setAnswers(new ArrayList<>());
        }

        // Create a set of answer IDs that should remain after the update
        Set<Long> updatedAnswerIds = answerDtos.stream()
                .filter(dto -> dto.getId() != null)
                .map(dto -> Long.parseLong(dto.getId()))
                .collect(Collectors.toSet());

        // Remove answers that are no longer in the submission
        question.getAnswers().removeIf(answer -> !updatedAnswerIds.contains(answer.getId()));

        // Process answers - update existing ones or add new ones
        for (SurveyAnswerUpsertDto answerDto : answerDtos) {
            if (answerDto.getId() != null) {
                // Update existing answer
                OperatorSurveyAnswer answer = question.getAnswers().stream()
                        .filter(a -> a.getId().equals(Long.parseLong(answerDto.getId())))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException(ErrorResponse.builder()
                                .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                                .message(ErrorMessage.NOT_FOUND.getMessage())
                                .fieldError("answerId")
                                .build()));

                answer.setAnswerText(answerDto.getAnswerText());
            } else {
                // Create new answer
                OperatorSurveyAnswer answer = SURVEY_MAPPER.toOpSurveyAnswerEntity(answerDto);
                answer.setQuestion(question);
                // Set isFixed flag
                if (answerDto.getAnswerText() == null || answerDto.getAnswerText().isEmpty()) {
                    answer.setAnswerText("");
                    answer.setIsFixed(false);
                } else {
                    answer.setIsFixed(true);
                }
                question.getAnswers().add(answer);
            }
        }
    }

    private void processOemSurveyQuestion(OemSurvey survey, SurveyQuestionsUpsertDto questionDto) {
        OemSurveyQuestion question;

        if (questionDto.getId() != null) {
            // Update existing question
            question = survey.getQuestions().stream()
                    .filter(q -> q.getId().equals(Long.parseLong(questionDto.getId())))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                            .message(ErrorMessage.NOT_FOUND.getMessage())
                            .fieldError("questionId")
                            .build()));

            // Check if question type has changed
            String oldType = question.getType();
            SURVEY_MAPPER.updateOemSurveyQuestion(questionDto, question);

            // If type changed to FREE_TEXT, clear all answers
            if (QuestionType.FREE_TEXT.getId().equals(question.getType()) && !QuestionType.FREE_TEXT.getId().equals(oldType)) {
                if (question.getAnswers() != null) {
                    question.getAnswers().clear();
                }
                return;
            }
        } else {
            // Create new question
            question = SURVEY_MAPPER.toOemSurveyQuestionEntity(questionDto);
            question.setSurvey(survey);
            if (survey.getQuestions() == null) {
                survey.setQuestions(new ArrayList<>());
            }
            survey.getQuestions().add(question);
        }

        // Process answers only if not FREE_TEXT
        if (!QuestionType.FREE_TEXT.getId().equals(question.getType())) {
            processOemSurveyAnswers(question, questionDto.getAnswers());
        }
    }

    private void processOemSurveyAnswers(OemSurveyQuestion question, List<SurveyAnswerUpsertDto> answerDtos) {
        if (QuestionType.FREE_TEXT.getId().equals(question.getType()) ||
                answerDtos == null || answerDtos.isEmpty()) {
            // If question is FREE_TEXT, no need to process answers
            return;
        }

        if (question.getAnswers() == null) {
            question.setAnswers(new ArrayList<>());
        }

        // Create a set of answer IDs that should remain after the update
        Set<Long> updatedAnswerIds = answerDtos.stream()
                .filter(dto -> dto.getId() != null)
                .map(dto -> Long.parseLong(dto.getId()))
                .collect(Collectors.toSet());

        // Remove answers that are no longer in the submission
        question.getAnswers().removeIf(answer -> !updatedAnswerIds.contains(answer.getId()));

        // Process answers - update existing ones or add new ones
        for (SurveyAnswerUpsertDto answerDto : answerDtos) {
            if (answerDto.getId() != null) {
                // Update existing answer
                OemSurveyAnswer answer = question.getAnswers().stream()
                        .filter(a -> a.getId().equals(Long.parseLong(answerDto.getId())))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException(ErrorResponse.builder()
                                .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                                .message(ErrorMessage.NOT_FOUND.getMessage())
                                .fieldError("answerId")
                                .build()));

                answer.setAnswerText(answerDto.getAnswerText());
            } else {
                // Create new answer
                OemSurveyAnswer answer = SURVEY_MAPPER.toOemSurveyAnswerEntity(answerDto);
                answer.setQuestion(question);

                // Set isFixed flag
                if (answerDto.getAnswerText() == null || answerDto.getAnswerText().isEmpty()) {
                    answer.setAnswerText("");
                    answer.setIsFixed(false);
                } else {
                    answer.setIsFixed(true);
                }

                question.getAnswers().add(answer);
            }
        }
    }

    @Transactional
    public void deleteSelectedSurveys(SelectedIds selectedIds) {
        if (selectedIds.getSelectedIds() == null || selectedIds.getSelectedIds().isEmpty()) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message(String.format(ErrorMessage.INVALID_DATA
                            .getStatusCode(), TargetName.SURVEY.getTargetName()))
                    .fieldError("selectedIds")
                    .build());
        }

        Account currentAccount = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(currentAccount);

        switch (SubRole.fromId(currentAccount.getSubRole())) {
            case OPERATOR -> {
                List<OperatorSurvey> surveys = operatorSurveyRepo.findInIds(
                        selectedIds.getSelectedIds(), parentInfo.getLeft());

                // Check for any non-deletable surveys
                for (OperatorSurvey survey : surveys) {
                    if (!survey.getIsDeletable()) {
                        throw new BadValidationException(ErrorResponse.builder()
                                .statusCode(ErrorMessage.INVALID_DELETION.getStatusCode())
                                .message(String.format(
                                        ErrorMessage.INVALID_DELETION.getMessage(),
                                        TargetName.SURVEY.getTargetName(),
                                        survey.getId()))
                                .fieldError("selectedIds")
                                .build());
                    }
                }

                List<String> surveyIds = surveys.stream()
                        .map(OperatorSurvey::getId)
                        .toList();

                if (!surveyIds.isEmpty()) {
                    // Execute bulk deletes in the correct order to maintain referential integrity
                    operatorSurveyAnswerRepo.deleteAnswersBySurveyIds(surveyIds);
                    operatorSurveyQuestionRepo.deleteQuestionsBySurveyIds(surveyIds);
                    operatorSurveyRepo.deleteSurveysByIds(surveyIds);
                }
            }
            case OEM -> {
                List<OemSurvey> surveys = oemSurveyRepo.findInIds(
                        selectedIds.getSelectedIds(), parentInfo.getLeft(), parentInfo.getRight());

                // Check for any non-deletable surveys
                for (OemSurvey survey : surveys) {
                    if (!survey.getIsDeletable()) {
                        throw new BadValidationException(ErrorResponse.builder()
                                .statusCode(ErrorMessage.INVALID_DELETION.getStatusCode())
                                .message(String.format(
                                        ErrorMessage.INVALID_DELETION.getMessage(),
                                        TargetName.SURVEY.getTargetName(),
                                        survey.getId()))
                                .fieldError("selectedIds")
                                .build());
                    }
                }

                List<String> surveyIds = surveys.stream()
                        .map(OemSurvey::getId)
                        .toList();

                if (!surveyIds.isEmpty()) {
                    // Execute bulk deletes in the correct order to maintain referential integrity
                    oemSurveyAnswerRepo.deleteAnswersBySurveyIds(surveyIds);
                    oemSurveyQuestionRepo.deleteQuestionsBySurveyIds(surveyIds);
                    oemSurveyRepo.deleteSurveysByIds(surveyIds);
                }
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public MSurveyListDto getSurveysForChat(SurveySearchDto surveySearchDto) {
        Pageable pageable = GeneralUtils.getPagination(
                surveySearchDto.getPageNumber(),
                surveySearchDto.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        String searchPattern = GeneralUtils.wrapToLike(surveySearchDto.getSearchInput());
        String applicantId = surveySearchDto.getApplicantId();

        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                Page<OperatorSurvey> operatorSurveys = operatorSurveyRepo.findAllUnsubmittedByApplicantId(
                        parentInfo.getLeft(),
                        searchPattern,
                        applicantId,
                        pageable);
                List<MSurveyItemsDto> items = operatorSurveys.getContent().stream()
                        .map(SURVEY_MAPPER::toMSurveyItemsDto)
                        .toList();

                return MSurveyListDto.builder()
                        .items(items)
                        .total(operatorSurveys.getTotalElements())
                        .page(operatorSurveys.getNumber() + 1)
                        .limit(operatorSurveys.getSize())
                        .build();
            }
            case OEM -> {
                Page<OemSurvey> oemSurveys = oemSurveyRepo.findAllUnsubmittedByApplicantId(
                        parentInfo.getLeft(),
                        parentInfo.getRight(),
                        searchPattern,
                        applicantId,
                        pageable);
                List<MSurveyItemsDto> items = oemSurveys.getContent().stream()
                        .map(SURVEY_MAPPER::toMSurveyItemsDto)
                        .toList();

                return MSurveyListDto.builder()
                        .items(items)
                        .total(oemSurveys.getTotalElements())
                        .page(oemSurveys.getNumber() + 1)
                        .limit(oemSurveys.getSize())
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public SurveyDetailsDto getQuestionsForPublicLink(String token) {
        // Decode token to get applicantSurveyId
        String applicantSurveyId = decodeSurveyToken(token);

        // First check if it's an operator survey
        Optional<OperatorApplicantSurvey> operatorApplicantSurvey = operatorApplicantSurveyRepo
                .findById(applicantSurveyId);
        if (operatorApplicantSurvey.isPresent()) {
            OperatorApplicantSurvey applicantSurvey = operatorApplicantSurvey.get();

            // Already submitted
            if (applicantSurvey.getRepliedAt() != null) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message("アンケートは既に提出されています")
                        .fieldError("token")
                        .build());
            }

            return getOperatorSurveyQuestionsForPublicLink(applicantSurvey);
        }

        // If not found in operator, check in OEM
        Optional<OemApplicantSurvey> oemApplicantSurvey = oemApplicantSurveyRepo
                .findById(applicantSurveyId);

        if (oemApplicantSurvey.isPresent()) {
            OemApplicantSurvey applicantSurvey = oemApplicantSurvey.get();

            // Already submitted
            if (applicantSurvey.getRepliedAt() != null) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message("アンケートは既に提出されています")
                        .fieldError("token")
                        .build());
            }

            return getOemSurveyQuestionsForPublicLink(applicantSurvey);
        }

        // If not found in either, throw NotFoundException
        throw new NotFoundException(ErrorResponse.builder()
                .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                .message(String.format(
                        ErrorMessage.NOT_FOUND.getMessage(),
                        TargetName.SURVEY.getTargetName()))
                .fieldError("token")
                .build());
    }

    @Transactional
    public SurveyLinkDto getSurveyPublicLink(SurveyLinkRequestDto surveyLinkRequestDto) {
        Account currentAccount = GeneralUtils.getCurrentUser();
        String applicantId = surveyLinkRequestDto.getApplicantId();
        String surveyId = surveyLinkRequestDto.getSurveyId();

        String link;

        switch (SubRole.fromId(currentAccount.getSubRole())) {
            case OPERATOR -> {
                link = handleOperatorSurveyLink(applicantId, surveyId, currentAccount);
            }
            case OEM -> {
                link = handleOemSurveyLink(applicantId, surveyId, currentAccount);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }

        return SurveyLinkDto.builder()
                .link(link)
                .build();
    }

    @Transactional
    public void submitSurvey(SurveySubmissionDto surveySubmissionDto) {
        // Decode token to get applicantSurveyId
        String token = surveySubmissionDto.getToken();
        String applicantSurveyId = decodeSurveyToken(token);

        boolean isHandled = handleOpApplicantSubmission(applicantSurveyId, surveySubmissionDto.getContent());
        if (isHandled) {
            return;
        }

        isHandled = handleOemApplicantSubmission(applicantSurveyId, surveySubmissionDto.getContent());
        if (isHandled) {
            return;
        }

        // If not found in either, throw NotFoundException
        throw new NotFoundException(ErrorResponse.builder()
                .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                .message(String.format(
                        ErrorMessage.NOT_FOUND.getMessage(),
                        TargetName.SURVEY.getTargetName()))
                .fieldError("token")
                .build());
    }

    private String handleOperatorSurveyLink(String applicantId, String surveyId, Account account) {
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        // Verify survey exists and belongs to the operator
        OperatorSurvey survey = getExistingOperatorSurveyById(surveyId, parentInfo.getLeft());

        // verify applicant exists and belongs to the operator
        Optional<OperatorApplicant> opApplicant = operatorApplicantRepo
                .findByIdAndParentId(applicantId, parentInfo.getLeft());
        if (opApplicant.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(ErrorMessage.NOT_FOUND.getMessage())
                    .fieldError("applicantId")
                    .build());
        }
        OperatorApplicant applicant = opApplicant.get();

        // Check if survey is already sent to this applicant
        Optional<OperatorApplicantSurvey> existingApplicantSurvey = operatorApplicantSurveyRepo
                .findBySurveyIdAndApplicantId(surveyId, applicantId);

        OperatorApplicantSurvey applicantSurvey;

        if (existingApplicantSurvey.isPresent()) {
            applicantSurvey = existingApplicantSurvey.get();
            if (applicantSurvey.getRepliedAt() != null) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message("アンケートは既に提出されています")
                        .fieldError("surveyId")
                        .build());
            }
            return applicantSurvey.getUrl();
        }

        // Create new applicant survey record
        applicantSurvey = new OperatorApplicantSurvey();
        applicantSurvey.setSurveyId(survey.getId());
        applicantSurvey.setApplicantId(applicant.getId());
        applicantSurvey.setSentAt(LocalDateTime.now());
        applicantSurvey.setUrl(Strings.EMPTY);

        applicantSurvey = operatorApplicantSurveyRepo.save(applicantSurvey);

        String token = generateSurveyToken(applicantSurvey.getId(), "operator");
        String surveyUrl = String.format("%s/applicant-survey?token=%s", frontEndUrl, token);
        applicantSurvey.setUrl(surveyUrl);
        operatorApplicantSurveyRepo.save(applicantSurvey);

        if (survey.getIsDeletable()) {
            // Set survey as non-deletable once it's sent to an applicant
            survey.setIsDeletable(false);
            operatorSurveyRepo.save(survey);
        }

        return surveyUrl;
    }

    private String handleOemSurveyLink(String applicantId, String surveyId, Account account) {
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        // Verify survey exists and belongs to the OEM
        OemSurvey survey = getExistingOemSurveyById(surveyId, parentInfo.getLeft(), parentInfo.getRight());

        // verify applicant exists and belongs to the operator
        Optional<OemApplicant> opApplicant = oemApplicantRepo
                .findByIdAndParentIdAndOemGroupId(applicantId, parentInfo.getLeft(), parentInfo.getRight());
        if (opApplicant.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(ErrorMessage.NOT_FOUND.getMessage())
                    .fieldError("applicantId")
                    .build());
        }
        OemApplicant applicant = opApplicant.get();

        // Check if survey is already sent to this applicant
        Optional<OemApplicantSurvey> existingApplicantSurvey = oemApplicantSurveyRepo
                .findBySurveyIdAndApplicantId(surveyId, applicantId);

        OemApplicantSurvey applicantSurvey;

        if (existingApplicantSurvey.isPresent()) {
            applicantSurvey = existingApplicantSurvey.get();
            if (applicantSurvey.getRepliedAt() != null) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message("アンケートは既に提出されています")
                        .fieldError("surveyId")
                        .build());
            }
            return applicantSurvey.getUrl();
        }

        // Create new applicant survey record
        applicantSurvey = new OemApplicantSurvey();
        applicantSurvey.setSurveyId(survey.getId());
        applicantSurvey.setApplicantId(applicant.getId());
        applicantSurvey.setSentAt(LocalDateTime.now());
        applicantSurvey.setUrl(Strings.EMPTY);

        applicantSurvey = oemApplicantSurveyRepo.save(applicantSurvey);

        String token = generateSurveyToken(applicantSurvey.getId(), "oem");
        String surveyUrl = String.format("%s/applicant-survey?token=%s", frontEndUrl, token);
        applicantSurvey.setUrl(surveyUrl);
        oemApplicantSurveyRepo.save(applicantSurvey);

        if (survey.getIsDeletable()) {
            // Set survey as non-deletable once it's sent to an applicant
            survey.setIsDeletable(false);
            oemSurveyRepo.save(survey);
        }

        return surveyUrl;
    }

    private String generateSurveyToken(String applicantSurveyId, String type) {
        // Create a token that encodes the applicant survey ID and type
        String rawToken = String.format("%s:%s:%s", type, applicantSurveyId, UUID.randomUUID().toString());

        // Encode the token to make it URL-safe and between 32-64 characters
        String encodedToken = Base64.getUrlEncoder().encodeToString(rawToken.getBytes());

        // Ensure token length is between 32-64 characters
        if (encodedToken.length() < 32) {
            // Add padding if token is too short (unlikely with UUID)
            String padding = UUID.randomUUID().toString().replaceAll("-", "");
            rawToken = rawToken + ":" + padding.substring(0, 32 - encodedToken.length());
            encodedToken = Base64.getUrlEncoder().encodeToString(rawToken.getBytes());
        } else if (encodedToken.length() > 64) {
            // Truncate if token is too long
            encodedToken = encodedToken.substring(0, 64);
        }

        return encodedToken;
    }

    public String decodeSurveyToken(String token) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(token);
        String decodedToken = new String(decodedBytes);

        // Expected format: "type:applicantSurveyId:uuid[:padding]"
        String[] parts = decodedToken.split(":");
        if (parts.length >= 2) {
            return parts[1]; // Return the applicantSurveyId
        }

        throw new BadValidationException(ErrorResponse.builder()
                .statusCode(ErrorMessage.INVALID_OPERATOR.getStatusCode())
                .message(ErrorMessage.INVALID_OPERATOR.getMessage())
                .fieldError("token")
                .build());
    }

    private boolean handleOpApplicantSubmission(String applicantSurveyId, List<AnswerSubmissionDto> answersDto) {
        // First check if applicantSurveyId exists in OperatorApplicantSurvey
        Optional<OperatorApplicantSurvey> operatorApplicantSurvey = operatorApplicantSurveyRepo
                .findById(applicantSurveyId);

        if (operatorApplicantSurvey.isEmpty()) {
            return false;
        }

        OperatorApplicantSurvey applicantSurvey = operatorApplicantSurvey.get();
        if (applicantSurvey.getRepliedAt() != null) {
            // Already submitted
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message("アンケートは既に提出されています")
                    .fieldError("token")
                    .build());
        }

        // Get the survey associated with this applicant survey
        String surveyId = applicantSurvey.getSurveyId();
        OperatorSurvey survey = operatorSurveyRepo.findById(surveyId)
                .orElseThrow(() -> new NotFoundException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                        .message(String.format(
                                ErrorMessage.NOT_FOUND.getMessage(),
                                TargetName.SURVEY.getTargetName()))
                        .fieldError("token")
                        .build()));

        // Create a map of question IDs for efficient lookup
        Map<Long, OperatorSurveyQuestion> questionMap = survey.getQuestions().stream()
                .collect(Collectors.toMap(OperatorSurveyQuestion::getId, Function.identity()));

        // create a map of questionId to List<OperatorSurveyAnswer> for validation
        List<OperatorSurveyAnswer> dbAnswers = operatorSurveyAnswerRepo
                .findByQuestionIdIn(new ArrayList<>(questionMap.keySet()));
        Map<Long, List<OperatorSurveyAnswer>> dbAnswersMap = dbAnswers.stream()
                .collect(Collectors.groupingBy(answer -> answer.getQuestion().getId()));

        // Create a map of answered questions for validation
        Map<Long, List<AnswerSubmissionDto>> answeredQuestionsMap = answersDto.stream()
                .collect(Collectors.groupingBy(answer -> Long.parseLong(answer.getQuestionId())));

        // First, check all required questions are answered
        validateRequiredOperatorQuestions(questionMap, answeredQuestionsMap, dbAnswersMap);

        // Process answers and create answer records
        List<OperatorApplicantAnswer> answers = new ArrayList<>();

        for (AnswerSubmissionDto answerDto : answersDto) {
            Long questionId = Long.parseLong(answerDto.getQuestionId());
            OperatorSurveyQuestion question = questionMap.get(questionId);

            // Skip if question doesn't exist in this survey
            if (question == null) {
                continue;
            }

            // Create answer record based on question type
            if (question.getType().equals(QuestionType.FREE_TEXT.getId())) {
                // For FREE_TEXT questions
                OperatorApplicantAnswer answer = new OperatorApplicantAnswer();
                answer.setQuestionId(questionId);
                answer.setApplicantSurveyId(applicantSurvey.getId());
                answer.setCreatedAt(LocalDateTime.now());
                answer.setAnswerText(answerDto.getAnswerText());
                answer.setSelectedAnswerId(null);
                answers.add(answer);
            } else {
                // For SINGLE or MULTIPLE choice questions
                List<String> answerIds = answerDto.getAnswerIds();

                List<OperatorSurveyAnswer> dbAnswersList = dbAnswersMap.get(questionId);

                if (answerIds != null && !answerIds.isEmpty()) {
                    for (String answerId : answerIds) {
                        Long selectedAnswerId = Long.parseLong(answerId);

                        // check if the answerId is valid
                        boolean isValidAnswer = dbAnswersList.stream()
                                .anyMatch(a -> a.getId().equals(selectedAnswerId));
                        if (!isValidAnswer) {
                            throw new BadValidationException(ErrorResponse.builder()
                                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                    .message("質問に対して無効な回答選択です")
                                    .fieldError("answerId")
                                    .build());
                        }

                        // Create a new answer record for each selected option
                        OperatorApplicantAnswer answer = new OperatorApplicantAnswer();
                        answer.setQuestionId(questionId);
                        answer.setApplicantSurveyId(applicantSurvey.getId());
                        answer.setCreatedAt(LocalDateTime.now());
                        answer.setAnswerText(answerDto.getAnswerText());
                        answer.setSelectedAnswerId(selectedAnswerId);
                        answers.add(answer);
                    }
                }
            }
        }

        // Update the applicant survey with completion timestamp
        applicantSurvey.setRepliedAt(LocalDateTime.now());
        operatorApplicantSurveyRepo.save(applicantSurvey);

        // Save all the answer records
        if (!answers.isEmpty()) {
            operatorApplicantAnswerRepo.saveAll(answers);
        }

        return true;
    }

    private void validateRequiredOperatorQuestions(
            Map<Long, OperatorSurveyQuestion> questionMap,
            Map<Long, List<AnswerSubmissionDto>> answeredQuestionsMap,
            Map<Long, List<OperatorSurveyAnswer>> dbAnswersMap) {
        for (OperatorSurveyQuestion question : questionMap.values()) {
            if (question.getIsRequired()) {
                // Check if this required question has an answer
                if (!answeredQuestionsMap.containsKey(question.getId())) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                            .message("必須質問に回答してください")
                            .fieldError("questionId")
                            .build());
                }

                // Get the answer for this required question
                List<AnswerSubmissionDto> answerDtos = answeredQuestionsMap.get(question.getId());

                // Validate based on question type
                switch (QuestionType.fromId(question.getType())) {
                    case FREE_TEXT -> {
                        // FREE_TEXT should have exactly one answer
                        if (answerDtos.size() > 1) {
                            throw new BadValidationException(ErrorResponse.builder()
                                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                    .message("自由記述方式の質問には一つの回答のみが必要です")
                                    .fieldError("questionId")
                                    .build());
                        }

                        AnswerSubmissionDto answerDto = answerDtos.get(0);
                        if (answerDto.getAnswerText() == null || answerDto.getAnswerText().isEmpty()) {
                            throw new BadValidationException(ErrorResponse.builder()
                                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                    .message("自由記述方式の質問には回答テキストが必要です")
                                    .fieldError("questionId")
                                    .build());
                        }
                    }
                    case SINGLE -> {
                        // SINGLE choice should have exactly one answer with exactly one selected option
                        if (answerDtos.size() > 1) {
                            throw new BadValidationException(ErrorResponse.builder()
                                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                    .message("選択方式の質問には一つの回答のみが必要です")
                                    .fieldError("questionId")
                                    .build());
                        }

                        AnswerSubmissionDto answerDto = answerDtos.get(0);
                        List<String> answerIds = answerDto.getAnswerIds();

                        // For SINGLE choice, must have exactly one option selected
                        if (answerIds == null || answerIds.isEmpty()) {
                            throw new BadValidationException(ErrorResponse.builder()
                                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                    .message("選択方式の質問には回答IDが必要です")
                                    .fieldError("questionId")
                                    .build());
                        }

                        if (answerIds.size() > 1) {
                            throw new BadValidationException(ErrorResponse.builder()
                                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                    .message("単一選択方式の質問には一つの選択肢のみが必要です")
                                    .fieldError("questionId")
                                    .build());
                        }

                        // Validate the selected option
                        validateOperatorChoiceAnswer(answerDto, question.getId(), dbAnswersMap);
                    }
                    case MULTIPLE -> {
                        // MULTIPLE choice can have multiple answers or one answer with multiple selections
                        if (answerDtos.size() == 1) {
                            // Case: One answer submission with multiple selected options
                            AnswerSubmissionDto answerDto = answerDtos.get(0);
                            validateOperatorChoiceAnswer(answerDto, question.getId(), dbAnswersMap);
                        } else {
                            // Case: Multiple answer submissions (older format or mixed format)
                            for (AnswerSubmissionDto answerDto : answerDtos) {
                                // Handle both new format (answerIds) and legacy format (answerId) for backward compatibility
                                List<String> answerIds = answerDto.getAnswerIds();
                                if (answerIds == null || answerIds.isEmpty()) {
                                    throw new BadValidationException(ErrorResponse.builder()
                                            .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                            .message("複数選択方式の質問には少なくとも一つの選択肢が必要です")
                                            .fieldError("questionId")
                                            .build());
                                }
                                validateOperatorChoiceAnswer(answerDto, question.getId(), dbAnswersMap);
                            }
                        }
                    }
                    default -> {
                        throw new BadValidationException(ErrorResponse.builder()
                                .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                .message("未知の質問タイプ: " + question.getType())
                                .fieldError("questionType")
                                .build());
                    }
                }
            }
        }
    }

    private void validateOperatorChoiceAnswer(AnswerSubmissionDto answerDto, Long questionId,
                                              Map<Long, List<OperatorSurveyAnswer>> dbAnswersMap) {
        List<String> answerIds = answerDto.getAnswerIds();

        if (answerIds == null || answerIds.isEmpty()) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message("選択方式の質問には回答IDが必要です")
                    .fieldError("questionId")
                    .build());
        }

        List<OperatorSurveyAnswer> availableAnswers = dbAnswersMap.get(questionId);

        // Validate each answer ID against the available answers
        for (String answerId : answerIds) {
            if (answerId == null || answerId.isEmpty()) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message("選択方式の質問には有効な回答IDが必要です")
                        .fieldError("answerId")
                        .build());
            }

            // Validate the answer ID against the available answers
            boolean isValidAnswer = availableAnswers.stream()
                    .anyMatch(a -> a.getId().equals(Long.parseLong(answerId)));
            if (!isValidAnswer) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message("質問に対して無効な回答選択です")
                        .fieldError("answerId")
                        .build());
            }
        }
    }

    private boolean handleOemApplicantSubmission(String applicantSurveyId, List<AnswerSubmissionDto> answersDto) {
        // First check if applicantSurveyId exists in OemApplicantSurvey
        Optional<OemApplicantSurvey> oemApplicantSurvey = oemApplicantSurveyRepo
                .findById(applicantSurveyId);

        if (oemApplicantSurvey.isEmpty()) {
            return false;
        }

        OemApplicantSurvey applicantSurvey = oemApplicantSurvey.get();
        if (applicantSurvey.getRepliedAt() != null) {
            // Already submitted
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message("アンケートは既に提出されています")
                    .fieldError("token")
                    .build());
        }

        // Get the survey associated with this applicant survey
        String surveyId = applicantSurvey.getSurveyId();
        OemSurvey survey = oemSurveyRepo.findById(surveyId)
                .orElseThrow(() -> new NotFoundException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                        .message(String.format(
                                ErrorMessage.NOT_FOUND.getMessage(),
                                TargetName.SURVEY.getTargetName()))
                        .fieldError("token")
                        .build()));

        // Create a map of question IDs for efficient lookup
        Map<Long, OemSurveyQuestion> questionMap = survey.getQuestions().stream()
                .collect(Collectors.toMap(OemSurveyQuestion::getId, Function.identity()));

        // create a map of questionId to List<OemSurveyAnswer> for validation
        List<OemSurveyAnswer> dbAnswers = oemSurveyAnswerRepo.findByQuestionIdIn(new ArrayList<>(questionMap.keySet()));
        Map<Long, List<OemSurveyAnswer>> dbAnswersMap = dbAnswers.stream()
                .collect(Collectors.groupingBy(answer -> answer.getQuestion().getId()));

        // Create a map of answered questions for validation
        Map<Long, List<AnswerSubmissionDto>> answeredQuestionsMap = answersDto.stream()
                .collect(Collectors.groupingBy(answer -> Long.parseLong(answer.getQuestionId())));

        // First, check all required questions are answered
        validateRequiredOemQuestions(questionMap, answeredQuestionsMap, dbAnswersMap);

        // Process answers and create answer records
        List<OemApplicantAnswer> answers = new ArrayList<>();

        for (AnswerSubmissionDto answerDto : answersDto) {
            Long questionId = Long.parseLong(answerDto.getQuestionId());
            OemSurveyQuestion question = questionMap.get(questionId);

            // Skip if question doesn't exist in this survey
            if (question == null) {
                continue;
            }

            // Create answer record based on question type
            if (question.getType().equals(QuestionType.FREE_TEXT.getId())) {
                // For FREE_TEXT questions
                OemApplicantAnswer answer = new OemApplicantAnswer();
                answer.setQuestionId(questionId);
                answer.setApplicantSurveyId(applicantSurvey.getId());
                answer.setCreatedAt(LocalDateTime.now());
                answer.setAnswerText(answerDto.getAnswerText());
                answer.setSelectedAnswerId(null);
                answers.add(answer);
            } else {
                // For SINGLE or MULTIPLE choice questions
                List<String> answerIds = answerDto.getAnswerIds();

                List<OemSurveyAnswer> dbAnswersList = dbAnswersMap.get(questionId);

                if (answerIds != null && !answerIds.isEmpty()) {
                    for (String answerId : answerIds) {
                        Long selectedAnswerId = Long.parseLong(answerId);

                        // check if the answerId is valid
                        boolean isValidAnswer = dbAnswersList.stream()
                                .anyMatch(a -> a.getId().equals(selectedAnswerId));
                        if (!isValidAnswer) {
                            throw new BadValidationException(ErrorResponse.builder()
                                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                    .message("質問に対して無効な回答選択です")
                                    .fieldError("answerId")
                                    .build());
                        }

                        // Create a new answer record for each selected option
                        OemApplicantAnswer answer = new OemApplicantAnswer();
                        answer.setQuestionId(questionId);
                        answer.setApplicantSurveyId(applicantSurvey.getId());
                        answer.setCreatedAt(LocalDateTime.now());
                        answer.setAnswerText(answerDto.getAnswerText());
                        answer.setSelectedAnswerId(selectedAnswerId);
                        answers.add(answer);
                    }
                }
            }
        }

        // Update the applicant survey with completion timestamp
        applicantSurvey.setRepliedAt(LocalDateTime.now());
        oemApplicantSurveyRepo.save(applicantSurvey);

        // Save all the answer records
        if (!answers.isEmpty()) {
            oemApplicantAnswerRepo.saveAll(answers);
        }

        return true;
    }

    private void validateRequiredOemQuestions(
            Map<Long, OemSurveyQuestion> questionMap,
            Map<Long, List<AnswerSubmissionDto>> answeredQuestionsMap,
            Map<Long, List<OemSurveyAnswer>> dbAnswersMap) {
        for (OemSurveyQuestion question : questionMap.values()) {
            if (question.getIsRequired()) {
                // Check if this required question has an answer
                if (!answeredQuestionsMap.containsKey(question.getId())) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                            .message("必須質問に回答してください")
                            .fieldError("questionId")
                            .build());
                }

                // Get the answer for this required question
                List<AnswerSubmissionDto> answerDtos = answeredQuestionsMap.get(question.getId());

                // Validate based on question type
                switch (QuestionType.fromId(question.getType())) {
                    case FREE_TEXT -> {
                        // FREE_TEXT should have exactly one answer
                        if (answerDtos.size() > 1) {
                            throw new BadValidationException(ErrorResponse.builder()
                                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                    .message("自由記述方式の質問には一つの回答のみが必要です")
                                    .fieldError("questionId")
                                    .build());
                        }

                        AnswerSubmissionDto answerDto = answerDtos.get(0);
                        if (answerDto.getAnswerText() == null || answerDto.getAnswerText().isEmpty()) {
                            throw new BadValidationException(ErrorResponse.builder()
                                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                    .message("自由記述方式の質問には回答テキストが必要です")
                                    .fieldError("questionId")
                                    .build());
                        }
                    }
                    case SINGLE -> {
                        // SINGLE choice should have exactly one answer with exactly one selected option
                        if (answerDtos.size() > 1) {
                            throw new BadValidationException(ErrorResponse.builder()
                                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                    .message("選択方式の質問には一つの回答のみが必要です")
                                    .fieldError("questionId")
                                    .build());
                        }

                        AnswerSubmissionDto answerDto = answerDtos.get(0);
                        List<String> answerIds = answerDto.getAnswerIds();

                        // For SINGLE choice, must have exactly one option selected
                        if (answerIds == null || answerIds.isEmpty()) {
                            throw new BadValidationException(ErrorResponse.builder()
                                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                    .message("選択方式の質問には回答IDが必要です")
                                    .fieldError("questionId")
                                    .build());
                        }

                        if (answerIds.size() > 1) {
                            throw new BadValidationException(ErrorResponse.builder()
                                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                    .message("単一選択方式の質問には一つの選択肢のみが必要です")
                                    .fieldError("questionId")
                                    .build());
                        }

                        // Validate the selected option
                        validateOemChoiceAnswer(answerDto, question.getId(), dbAnswersMap);
                    }
                    case MULTIPLE -> {
                        // MULTIPLE choice can have multiple answers or one answer with multiple selections
                        if (answerDtos.size() == 1) {
                            // Case: One answer submission with multiple selected options
                            AnswerSubmissionDto answerDto = answerDtos.get(0);
                            validateOemChoiceAnswer(answerDto, question.getId(), dbAnswersMap);
                        } else {
                            // Case: Multiple answer submissions (older format or mixed format)
                            for (AnswerSubmissionDto answerDto : answerDtos) {
                                // Handle both new format (answerIds) and legacy format (answerId) for backward compatibility
                                List<String> answerIds = answerDto.getAnswerIds();
                                if (answerIds == null || answerIds.isEmpty()) {
                                    throw new BadValidationException(ErrorResponse.builder()
                                            .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                            .message("複数選択方式の質問には少なくとも一つの選択肢が必要です")
                                            .fieldError("questionId")
                                            .build());
                                }
                                validateOemChoiceAnswer(answerDto, question.getId(), dbAnswersMap);
                            }
                        }
                    }
                    default -> {
                        throw new BadValidationException(ErrorResponse.builder()
                                .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                                .message("未知の質問タイプ: " + question.getType())
                                .fieldError("questionType")
                                .build());
                    }
                }
            }
        }
    }

    private void validateOemChoiceAnswer(AnswerSubmissionDto answerDto, Long questionId,
                                         Map<Long, List<OemSurveyAnswer>> dbAnswersMap) {
        List<String> answerIds = answerDto.getAnswerIds();

        if (answerIds == null || answerIds.isEmpty()) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message("選択方式の質問には回答IDが必要です")
                    .fieldError("questionId")
                    .build());
        }

        List<OemSurveyAnswer> availableAnswers = dbAnswersMap.get(questionId);

        // Validate each answer ID against the available answers
        for (String answerId : answerIds) {
            if (answerId == null || answerId.isEmpty()) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message("選択方式の質問には有効な回答IDが必要です")
                        .fieldError("answerId")
                        .build());
            }

            // Validate the answer ID against the available answers
            boolean isValidAnswer = availableAnswers.stream()
                    .anyMatch(a -> a.getId().equals(Long.parseLong(answerId)));
            if (!isValidAnswer) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message("質問に対して無効な回答選択です")
                        .fieldError("answerId")
                        .build());
            }
        }
    }

    private List<SurveyQuestionsDto> getOpSurveyQuestionList(List<OperatorSurveyQuestion> questions) {
        if (questions == null || questions.isEmpty()) {
            return List.of();
        }

        List<Long> questionIds = questions.stream()
                .map(OperatorSurveyQuestion::getId)
                .toList();

        Map<Long, List<OperatorSurveyAnswer>> answersMap = operatorSurveyAnswerRepo.findByQuestionIdIn(questionIds)
                .stream()
                .collect(Collectors.groupingBy(answer -> answer.getQuestion().getId()));

        return questions.stream()
                .map(entity -> {
                    SurveyQuestionsDto questionDto = SURVEY_MAPPER.toQuestionsDto(entity);
                    List<OperatorSurveyAnswer> answers = answersMap.getOrDefault(entity.getId(), List.of());
                    questionDto.setAnswers(answers.stream()
                            .map(SURVEY_MAPPER::toOpAnwsersDto)
                            .toList());
                    return questionDto;
                })
                .toList();
    }

    private List<SurveyQuestionsDto> getOemSurveyQuestionList(List<OemSurveyQuestion> questions) {
        if (questions == null || questions.isEmpty()) {
            return List.of();
        }

        List<Long> questionIds = questions.stream()
                .map(OemSurveyQuestion::getId)
                .toList();

        Map<Long, List<OemSurveyAnswer>> answersMap = oemSurveyAnswerRepo.findByQuestionIdIn(questionIds)
                .stream()
                .collect(Collectors.groupingBy(answer -> answer.getQuestion().getId()));

        return questions.stream()
                .map(entity -> {
                    SurveyQuestionsDto questionDto = SURVEY_MAPPER.toOemQuestionsDto(entity);
                    List<OemSurveyAnswer> answers = answersMap.getOrDefault(entity.getId(), List.of());
                    questionDto.setAnswers(answers.stream()
                            .map(SURVEY_MAPPER::toOemAnwsersDto)
                            .toList());
                    return questionDto;
                })
                .toList();
    }

    private SurveyDetailsDto getOperatorSurveyQuestionsForPublicLink(OperatorApplicantSurvey applicantSurvey) {
        String surveyId = applicantSurvey.getSurveyId();

        // Get the survey using the surveyId
        OperatorSurvey survey = operatorSurveyRepo.findById(surveyId)
                .orElseThrow(() -> new NotFoundException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                        .message(String.format(
                                ErrorMessage.NOT_FOUND.getMessage(),
                                TargetName.SURVEY.getTargetName()))
                        .fieldError("token")
                        .build()));

        // Create DTO from the survey
        SurveyDetailsDto surveyDetailsDto = SURVEY_MAPPER.toOpDetailsDto(survey);

        // Add questions and answers
        surveyDetailsDto.setContent(getOpSurveyQuestionList(survey.getQuestions()));

        return surveyDetailsDto;
    }

    private SurveyDetailsDto getOemSurveyQuestionsForPublicLink(OemApplicantSurvey applicantSurvey) {
        String surveyId = applicantSurvey.getSurveyId();

        // Get the survey using the surveyId
        OemSurvey survey = oemSurveyRepo.findById(surveyId)
                .orElseThrow(() -> new NotFoundException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                        .message(String.format(
                                ErrorMessage.NOT_FOUND.getMessage(),
                                TargetName.SURVEY.getTargetName()))
                        .fieldError("token")
                        .build()));

        // Create DTO from the survey
        SurveyDetailsDto surveyDetailsDto = SURVEY_MAPPER.toOemDetailsDto(survey);

        // Add questions and answers
        surveyDetailsDto.setContent(getOemSurveyQuestionList(survey.getQuestions()));

        return surveyDetailsDto;
    }
}
