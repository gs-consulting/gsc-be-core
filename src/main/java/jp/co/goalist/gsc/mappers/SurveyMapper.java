package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.QuestionType;
import jp.co.goalist.gsc.gen.dtos.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SurveyMapper {

    SurveyMapper SURVEY_MAPPER = Mappers.getMapper(SurveyMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "type", expression = "java(mapSurveyType(surveyUpsertDto.getType()))")
    OperatorSurveyQuestion toOpSurveyQuestionEntity(SurveyQuestionsUpsertDto surveyUpsertDto);

    @Mapping(target = "id", ignore = true)
    OperatorSurveyAnswer toOpSurveyAnswerEntity(SurveyAnswerUpsertDto surveyUpsertDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "type", expression = "java(mapSurveyType(surveyUpsertDto.getType()))")
    OemSurveyQuestion toOemSurveyQuestionEntity(SurveyQuestionsUpsertDto surveyUpsertDto);

    @Mapping(target = "id", ignore = true)
    OemSurveyAnswer toOemSurveyAnswerEntity(SurveyAnswerUpsertDto surveyUpsertDto);

    @Named("mapSurveyType")
    default String mapSurveyType(String surveyType) {
        return QuestionType.fromId(surveyType).getId();
    }

    // New methods for DTO mapping
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "surveyName")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    SurveyItemsDto toOperatorSurveyItemsDto(OperatorSurvey survey);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "surveyName")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    SurveyItemsDto toOemSurveyItemsDto(OemSurvey survey);

    @Mapping(target = "content", ignore = true)
    @Mapping(target = "name", source = "surveyName")
    @Mapping(target = "isEditable", source = "isDeletable")
    SurveyDetailsDto toOpDetailsDto(OperatorSurvey survey);

    @Mapping(target = "answers", ignore = true)
    SurveyQuestionsDto toQuestionsDto(OperatorSurveyQuestion question);

    SurveyAnswersDto toOpAnwsersDto(OperatorSurveyAnswer answer);

    @Mapping(target = "content", ignore = true)
    @Mapping(target = "name", source = "surveyName")
    @Mapping(target = "isEditable", source = "isDeletable")
    SurveyDetailsDto toOemDetailsDto(OemSurvey survey);

    @Mapping(target = "answers", ignore = true)
    SurveyQuestionsDto toOemQuestionsDto(OemSurveyQuestion question);

    SurveyAnswersDto toOemAnwsersDto(OemSurveyAnswer answer);

    @Mapping(target = "surveyName", source = "name")
    void updateOpSurvey(SurveyUpsertDto upsertDto, @MappingTarget OperatorSurvey survey);

    @Mapping(target = "surveyName", source = "name")
    void updateOemSurvey(SurveyUpsertDto upsertDto, @MappingTarget OemSurvey survey);

    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "type", expression = "java(mapSurveyType(questionUpsertDto.getType()))")
    void updateOpSurveyQuestion(SurveyQuestionsUpsertDto questionUpsertDto,
                                @MappingTarget OperatorSurveyQuestion question);

    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "type", expression = "java(mapSurveyType(questionUpsertDto.getType()))")
    void updateOemSurveyQuestion(SurveyQuestionsUpsertDto questionUpsertDto, @MappingTarget OemSurveyQuestion question);

    void updateOpSurveyAnswer(SurveyAnswerUpsertDto answerUpsertDto, @MappingTarget OperatorSurveyAnswer answer);

    void updateOemSurveyAnswer(SurveyAnswerUpsertDto answerUpsertDto, @MappingTarget OemSurveyAnswer answer);

    @Mapping(target = "name", source = "surveyName")
    MSurveyItemsDto toMSurveyItemsDto(OperatorSurvey survey);

    @Mapping(target = "name", source = "surveyName")
    MSurveyItemsDto toMSurveyItemsDto(OemSurvey survey);
}
