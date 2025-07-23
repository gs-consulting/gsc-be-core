package jp.co.goalist.gsc.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.entities.OemHistoryCall;
import jp.co.goalist.gsc.entities.OperatorHistoryCall;
import jp.co.goalist.gsc.gen.dtos.HistoryCallDetailsDto;
import jp.co.goalist.gsc.gen.dtos.HistoryCallItemsDto;
import jp.co.goalist.gsc.gen.dtos.HistoryCallUpsertDto;


@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface HistoryCallMapper extends BaseMapper {

    HistoryCallMapper HISTORY_CALL_MAPPER = Mappers.getMapper(HistoryCallMapper.class);

    @Mapping(target = "callStartDate", source = "contactStartDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "callEndDate", source = "contactEndDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "memo", source = "memo")
    @BeanMapping(ignoreByDefault = true)
    OperatorHistoryCall toOpHistoryCall(HistoryCallUpsertDto dto);

    @Mapping(target = "callStartDate", source = "contactStartDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "callEndDate", source = "contactEndDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "memo", source = "memo")
    @BeanMapping(ignoreByDefault = true)
    OemHistoryCall toOemHistoryCall(HistoryCallUpsertDto dto);

    @Mapping(target = "contactDateTime", source = "callStartDate", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "pic", source = "pic.clientName")
    HistoryCallItemsDto toHistoryCallItemsDto(OperatorHistoryCall operatorHistoryCall);

    @Mapping(target = "contactDateTime", source = "callStartDate", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "pic", source = "pic.clientName")
    HistoryCallItemsDto toHistoryCallItemsDto(OemHistoryCall operatorHistoryCall);

    @Mapping(target = "callStartDate", source = "contactStartDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "callEndDate", source = "contactEndDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "memo", source = "memo")
    void updateOpHistoryCall(HistoryCallUpsertDto dto, @MappingTarget OperatorHistoryCall operatorHistoryCall);

    @Mapping(target = "callStartDate", source = "contactStartDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "callEndDate", source = "contactEndDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "memo", source = "memo")
    void updateOemHistoryCall(HistoryCallUpsertDto dto, @MappingTarget OemHistoryCall oemHistoryCall);

    @Mapping(target = "contactStartDateTime", source = "callStartDate", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "contactEndDateTime", source = "callEndDate", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "picId", source = "pic.id")
    HistoryCallDetailsDto toHistoryCallDetailsDto(OperatorHistoryCall operatorHistoryCall);

    @Mapping(target = "contactStartDateTime", source = "callStartDate", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "contactEndDateTime", source = "callEndDate", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "picId", source = "pic.id")
    HistoryCallDetailsDto toHistoryCallDetailsDto(OemHistoryCall oemHistoryCall);
}
