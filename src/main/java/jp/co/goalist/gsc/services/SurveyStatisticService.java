package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.dtos.survey.ApplicantUnansweredItemDto;
import jp.co.goalist.gsc.dtos.survey.SurveyAnswerSummaryItemDto;
import jp.co.goalist.gsc.dtos.survey.SurveyApplicantAnswerItemDto;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.SubRole;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.*;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

import static jp.co.goalist.gsc.mappers.SurveyStatisticMapper.SURVEY_STATISTIC_MAPPER;

@Service
@RequiredArgsConstructor
public class SurveyStatisticService {

    private final Map<String, String> SURVEY_ANSWERS_SORT_MAP = new HashMap<>() {
        {
            put("answerDateTime", "answerDateTime");
            put("fullName", "fullName");
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
    private final OperatorSurveyQuestionRepository operatorSurveyQuestionRepo;
    private final OemSurveyQuestionRepository oemSurveyQuestionRepo;
    private final OperatorApplicantSurveyRepository operatorApplicantSurveyRepo;
    private final OemApplicantSurveyRepository oemApplicantSurveyRepo;
    private final OperatorApplicantAnswerRepository operatorApplicantAnswerRepo;
    private final OemApplicantAnswerRepository oemApplicantAnswerRepo;
    private final UtilService utilService;

    @Transactional
    public SurveyAnswersListDto getSurveyStatisticAnswers(String surveyId, SurveyStatisticSearchDto searchDto) {
        Pageable pageable = GeneralUtils.getPagination(
                searchDto.getPageNumber(),
                searchDto.getPageSize());
        Pageable pageableArrangedBy = GeneralUtils.getPagination(
                searchDto.getPageNumber(),
                Integer.MAX_VALUE,
                getSortBy(searchDto.getArrangedBy()));

        Pair<Long, Long> searchIds = validateSearchIds(searchDto.getQuestionId(), searchDto.getAnswerId());
        Account account = GeneralUtils.getCurrentUser();
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                // Get answers
                Page<String> applicantIds = operatorApplicantAnswerRepo.findApplicantIdsBySurveyId(
                        surveyId, pageable);
                Page<SurveyApplicantAnswerItemDto> applicantAnswers = operatorApplicantAnswerRepo.findAllBySurveyIdAndApplicantIds(
                        surveyId, applicantIds.getContent(), pageableArrangedBy);

                // Get question data and group answers
                List<Long> questionIds = operatorSurveyQuestionRepo.findAllQuestionIdsBySurveyId(surveyId);
                List<SurveyAnswersItemsDto> surveyAnswersItems = groupAnswers(applicantAnswers.getContent(), questionIds, searchIds.getLeft(), searchIds.getRight());

                return SurveyAnswersListDto.builder()
                        .totalQuestions(questionIds.size())
                        .items(surveyAnswersItems)
                        .total((long) surveyAnswersItems.size())
                        .page(applicantAnswers.getNumber() + 1)
                        .limit(applicantAnswers.getSize())
                        .build();
            }
            case OEM -> {
                // Get answers
                Page<String> applicantIds = oemApplicantAnswerRepo.findApplicantIdsBySurveyId(
                        surveyId, pageable);
                Page<SurveyApplicantAnswerItemDto> applicantAnswers = oemApplicantAnswerRepo.findAllBySurveyIdAndApplicantIds(
                        surveyId, applicantIds.getContent(), pageableArrangedBy);

                // Get question data and group answers
                List<Long> questionIds = oemSurveyQuestionRepo.findAllQuestionIdsBySurveyId(surveyId);
                List<SurveyAnswersItemsDto> surveyAnswersItems = groupAnswers(applicantAnswers.getContent(), questionIds, searchIds.getLeft(), searchIds.getRight());

                return SurveyAnswersListDto.builder()
                        .totalQuestions(questionIds.size())
                        .items(surveyAnswersItems)
                        .total(applicantIds.getTotalElements())
                        .page(applicantIds.getNumber() + 1)
                        .limit(applicantIds.getSize())
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public SurveyStatisticDto getSurveyStatisticsSummary(String surveyId) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                List<ApplicantUnansweredItemDto> unanswered = operatorApplicantSurveyRepo.findAllApplicantUnansweredBySurveyId(surveyId);
                OperatorSurvey surveyData = operatorSurveyRepo.findFetchAnswersById(surveyId, parentInfo.getLeft())
                        .orElseThrow(() -> new NotFoundException(ErrorResponse.builder().build()));
                List<SurveyAnswerSummaryItemDto> answerSummaryData = operatorApplicantAnswerRepo
                        .findAllAnswerSummaryBySurveyId(surveyId);

                return SurveyStatisticDto.builder()
                        .name(surveyData.getSurveyName())
                        .questions(summaryOperatorSurveyData(surveyData, answerSummaryData))
                        .unansweredApplicants(
                                unanswered
                                        .stream()
                                        .map(SURVEY_STATISTIC_MAPPER::toSurveyUnansweredItemsDto)
                                        .toList())
                        .build();
            }
            case OEM -> {
                List<ApplicantUnansweredItemDto> unanswered = oemApplicantSurveyRepo.findAllApplicantUnansweredBySurveyId(surveyId);
                OemSurvey surveyData = oemSurveyRepo.findFetchAnswersById(surveyId, parentInfo.getLeft(), parentInfo.getRight())
                        .orElseThrow(() -> new NotFoundException(ErrorResponse.builder().build()));
                List<SurveyAnswerSummaryItemDto> answerSummaryData = oemApplicantAnswerRepo
                        .findAllAnswerSummaryBySurveyId(surveyId);

                return SurveyStatisticDto.builder()
                        .name(surveyData.getSurveyName())
                        .questions(summaryOemSurveyData(surveyData, answerSummaryData))
                        .unansweredApplicants(
                                unanswered
                                        .stream()
                                        .map(SURVEY_STATISTIC_MAPPER::toSurveyUnansweredItemsDto)
                                        .toList())
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    private List<SurveyStatisticQuestionsDto> summaryOperatorSurveyData(OperatorSurvey surveyData, List<SurveyAnswerSummaryItemDto> answerSummaryData) {
        List<SurveyStatisticQuestionsDto> response = new ArrayList<>();
        List<OperatorSurveyQuestion> questions = surveyData.getQuestions();
        long applicantCount = answerSummaryData.stream().map(SurveyAnswerSummaryItemDto::getApplicantId).distinct().count();

        questions.forEach(question -> {
            // All answers for this question
            List<SurveyAnswerSummaryItemDto> actualAnswers = answerSummaryData.stream()
                    .filter(i -> i.getQuestionId().equals(question.getId())).toList();

            // Question & answer data
            List<OperatorSurveyAnswer> answers = question.getAnswers();
            boolean isFreeText = question.getAnswers().isEmpty();

            // Map question & answer data (for return)
            SurveyStatisticQuestionsDto statisticQuestion = SURVEY_STATISTIC_MAPPER.toSurveyStatisticQuestionsDto(question);
            List<SurveyStatisticAnswersDto> statisticAnswers = new ArrayList<>();

            // Calculate ratio of each answer
            if (isFreeText) {
                SurveyStatisticAnswersDto statisticAnswer = new SurveyStatisticAnswersDto();
                float answerPercent = actualAnswers.stream()
                        .filter(i -> !i.getFreeText().isEmpty()).count() * 100F;

                // Set and add this answer data to the question list
                statisticAnswer.setRatio(applicantCount == 0 ? 0 : GeneralUtils.round(answerPercent / applicantCount, 1));
                statisticAnswers.add(statisticAnswer);
            } else {
                long answeredApplicantCount = actualAnswers.stream().map(SurveyAnswerSummaryItemDto::getApplicantId).distinct().count();

                answers.forEach(answer -> {
                    SurveyStatisticAnswersDto statisticAnswer = SURVEY_STATISTIC_MAPPER.toSurveyStatisticAnswersDto(answer);
                    float answerPercent = actualAnswers.stream()
                            .filter(i -> i.getAnswerId().equals(answer.getId())).count() * 100F;

                    // Set and add this answer data to the question list
                    statisticAnswer.setRatio(applicantCount == 0 ? 0 : GeneralUtils.round(answerPercent / answeredApplicantCount, 1));
                    statisticAnswers.add(statisticAnswer);
                });
            }

            statisticQuestion.setAnswers(statisticAnswers);

            // Add question data to the response
            response.add(statisticQuestion);
        });

        return response;
    }

    private List<SurveyStatisticQuestionsDto> summaryOemSurveyData(OemSurvey surveyData, List<SurveyAnswerSummaryItemDto> answerSummaryData) {
        List<SurveyStatisticQuestionsDto> response = new ArrayList<>();
        List<OemSurveyQuestion> questions = surveyData.getQuestions();
        long applicantCount = answerSummaryData.stream().map(SurveyAnswerSummaryItemDto::getApplicantId).distinct().count();

        questions.forEach(question -> {
            // All answers for this question
            List<SurveyAnswerSummaryItemDto> actualAnswers = answerSummaryData.stream()
                    .filter(i -> i.getQuestionId().equals(question.getId())).toList();

            // Question & answer data
            List<OemSurveyAnswer> answers = question.getAnswers();
            boolean isFreeText = question.getAnswers().isEmpty();

            // Map question & answer data (for return)
            SurveyStatisticQuestionsDto statisticQuestion = SURVEY_STATISTIC_MAPPER.toSurveyStatisticQuestionsDto(question);
            List<SurveyStatisticAnswersDto> statisticAnswers = new ArrayList<>();

            // Calculate ratio of each answer
            if (isFreeText) {
                SurveyStatisticAnswersDto statisticAnswer = new SurveyStatisticAnswersDto();
                float answerPercent = actualAnswers.stream()
                        .filter(i -> !i.getFreeText().isEmpty()).count() * 100F;

                // Set and add this answer data to the question list
                statisticAnswer.setRatio(applicantCount == 0 ? 0 : GeneralUtils.round(answerPercent / applicantCount, 1));
                statisticAnswers.add(statisticAnswer);
            } else {
                long answeredApplicantCount = actualAnswers.stream().map(SurveyAnswerSummaryItemDto::getApplicantId).distinct().count();

                answers.forEach(answer -> {
                    SurveyStatisticAnswersDto statisticAnswer = SURVEY_STATISTIC_MAPPER.toSurveyStatisticAnswersDto(answer);
                    float answerPercent = actualAnswers.stream()
                            .filter(i -> i.getAnswerId().equals(answer.getId())).count() * 100F;

                    // Set and add this answer data to the question list
                    statisticAnswer.setRatio(applicantCount == 0 ? 0 : GeneralUtils.round(answerPercent / answeredApplicantCount, 1));
                    statisticAnswers.add(statisticAnswer);
                });
            }

            statisticQuestion.setAnswers(statisticAnswers);

            // Add question data to the response
            response.add(statisticQuestion);
        });

        return response;
    }

    private List<SurveyAnswersItemsDto> groupAnswers(List<SurveyApplicantAnswerItemDto> applicantAnswers, List<Long> questionIds, Long searchQId, Long searchAId) {
        if (applicantAnswers == null || applicantAnswers.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, SurveyAnswersItemsDto> applicantMap = new LinkedHashMap<>();
        Map<String, TreeMap<Long, String>> answerMap = new HashMap<>();
        Map<String, TreeMap<Long, List<Long>>> answerIdsMap = new HashMap<>();

        // Group answers by applicants and questions
        for (SurveyApplicantAnswerItemDto item : applicantAnswers) {
            String applicantId = item.getApplicantId();
            Long questionId = item.getQuestionId();
            Long answerId = item.getAnswerId();
            String answerText = StringUtils.defaultIfBlank(item.getAnswerText(), item.getFreeText());

            // Compute exiting applicant answer record
            applicantMap.computeIfAbsent(applicantId, id -> SURVEY_STATISTIC_MAPPER.toSurveyAnswersItemsDto(item));

            // Update answers for exiting record
            TreeMap<Long, String> thisAnswers = answerMap.computeIfAbsent(applicantId, id -> initAnswerMap(questionIds));
            thisAnswers.merge(questionId, answerText, (existing, incoming) -> {
                if (existing.isEmpty()) {
                    return incoming;
                } else {
                    return existing + "、" + incoming;
                }
            });

            // Use for filter
            TreeMap<Long, List<Long>> thisAnswerIds = answerIdsMap.computeIfAbsent(applicantId, id -> initAnswerIdsMap(questionIds));
            thisAnswerIds.merge(questionId, new ArrayList<>(Collections.singletonList(answerId)), (existing, incoming) -> {
                if (existing.isEmpty()) {
                    return incoming;
                } else {
                    existing.addAll(incoming);
                    return existing;
                }
            });
        }

        // Set answers list for each applicant
        Iterator<Map.Entry<String, SurveyAnswersItemsDto>> iterator = applicantMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SurveyAnswersItemsDto> entry = iterator.next();
            String applicantId = entry.getKey();
            SurveyAnswersItemsDto dto = entry.getValue();

            // Set answers list
            TreeMap<Long, String> answers = answerMap.get(applicantId);
            if (answers != null) {
                dto.setAnswers(new ArrayList<>(answers.values()));
            }

            // Apply search filter - remove if it doesn't match
            if (Objects.nonNull(searchQId)) {
                TreeMap<Long, List<Long>> applicantAnswerIds = answerIdsMap.get(applicantId);
                boolean matchesFilter = false;

                if (applicantAnswerIds != null && applicantAnswerIds.containsKey(searchQId)) {
                    List<Long> answerIds = applicantAnswerIds.get(searchQId);
                    matchesFilter = answerIds != null && answerIds.contains(searchAId);
                }

                if (!matchesFilter) {
                    iterator.remove(); // Remove from map if doesn't match filter
                }
            }
        }

        for (Map.Entry<String, SurveyAnswersItemsDto> entry : applicantMap.entrySet()) {
            String applicantId = entry.getKey();
            SurveyAnswersItemsDto dto = entry.getValue();

            TreeMap<Long, String> answers = answerMap.get(applicantId);
            if (answers != null) {
                dto.setAnswers(new ArrayList<>(answers.values()));
            }
        }

        // Return the Answer list (grouped by applicants)
        return new ArrayList<>(applicantMap.values());
    }

    private TreeMap<Long, String> initAnswerMap(List<Long> questionIds) {
        TreeMap<Long, String> map = new TreeMap<>();
        questionIds.forEach(id -> map.put(id, ""));
        return map;
    }

    private TreeMap<Long, List<Long>> initAnswerIdsMap(List<Long> questionIds) {
        TreeMap<Long, List<Long>> map = new TreeMap<>();
        questionIds.forEach(id -> map.put(id, new ArrayList<>()));
        return map;
    }

    private Pair<Long, Long> validateSearchIds(String qId, String aId) {
        if (Objects.nonNull(qId) && !qId.isEmpty()) {
            if (Objects.nonNull(aId) && !aId.isEmpty()) {
                return Pair.of(Long.parseLong(qId), Long.parseLong(aId));
            } else {
                return Pair.of(Long.parseLong(qId), null);
            }
        } else {
            return Pair.of(null, null);
        }
    }

    private Sort getSortBy(String arrangedBy) {
        if (arrangedBy == null) {
            return Sort.by(Sort.Direction.DESC, "answerDateTime");
        }
        String[] arrangedByArray = arrangedBy.split(":");
        if (arrangedByArray.length != 2 || !SURVEY_ANSWERS_SORT_MAP.containsKey(arrangedByArray[0]) ||
                !SORT_ORDERS.containsKey(arrangedByArray[1])) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_OPERATOR.getStatusCode())
                    .message(ErrorMessage.INVALID_OPERATOR.getMessage())
                    .fieldError("arrangedBy")
                    .build());
        }

        return Sort.by(Sort.Direction.fromString(SORT_ORDERS.get(arrangedByArray[1])),
                SURVEY_ANSWERS_SORT_MAP.get(arrangedByArray[0]));
    }
}
