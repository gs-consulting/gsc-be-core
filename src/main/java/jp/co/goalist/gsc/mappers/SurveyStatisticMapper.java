package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.dtos.survey.ApplicantUnansweredItemDto;
import jp.co.goalist.gsc.dtos.survey.SurveyApplicantAnswerItemDto;
import jp.co.goalist.gsc.entities.OemSurveyAnswer;
import jp.co.goalist.gsc.entities.OemSurveyQuestion;
import jp.co.goalist.gsc.entities.OperatorSurveyAnswer;
import jp.co.goalist.gsc.entities.OperatorSurveyQuestion;
import jp.co.goalist.gsc.gen.dtos.SurveyAnswersItemsDto;
import jp.co.goalist.gsc.gen.dtos.SurveyStatisticAnswersDto;
import jp.co.goalist.gsc.gen.dtos.SurveyStatisticQuestionsDto;
import jp.co.goalist.gsc.gen.dtos.SurveyUnansweredItemsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SurveyStatisticMapper {

    SurveyStatisticMapper SURVEY_STATISTIC_MAPPER = Mappers.getMapper(SurveyStatisticMapper.class);

    @Mapping(target = "id", source = "applicantId")
    @Mapping(target = "answers", ignore = true)
    SurveyAnswersItemsDto toSurveyAnswersItemsDto(SurveyApplicantAnswerItemDto answers);

    @Mapping(target = "id", source = "applicantId")
    @Mapping(target = "isDuplicate", expression = "java(item.getIsDuplicate() == 1)")
    @Mapping(target = "isValid", expression = "java(item.getIsValid() == 1)")
    @Mapping(target = "isBlacklist", expression = "java(item.getIsBlacklist() == 1)")
    SurveyUnansweredItemsDto toSurveyUnansweredItemsDto(ApplicantUnansweredItemDto item);

    @Mapping(target = "id", expression = "java(question.getId().toString())")
    @Mapping(target = "questionType", source = "type")
    @Mapping(target = "answers", ignore = true)
    SurveyStatisticQuestionsDto toSurveyStatisticQuestionsDto(OperatorSurveyQuestion question);

    @Mapping(target = "id", expression = "java(question.getId().toString())")
    @Mapping(target = "questionType", source = "type")
    @Mapping(target = "answers", ignore = true)
    SurveyStatisticQuestionsDto toSurveyStatisticQuestionsDto(OemSurveyQuestion question);

    @Mapping(target = "id", expression = "java(answer.getId().toString())")
    @Mapping(target = "ratio", ignore = true)
    SurveyStatisticAnswersDto toSurveyStatisticAnswersDto(OperatorSurveyAnswer answer);

    @Mapping(target = "id", expression = "java(answer.getId().toString())")
    @Mapping(target = "ratio", ignore = true)
    SurveyStatisticAnswersDto toSurveyStatisticAnswersDto(OemSurveyAnswer answer);
}
