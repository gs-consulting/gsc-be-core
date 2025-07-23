package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.dtos.SelectionStatusDto;
import jp.co.goalist.gsc.gen.dtos.MasterSelectionStatusItemsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import jp.co.goalist.gsc.entities.SelectionStatus;
import jp.co.goalist.gsc.enums.FlowType;
import jp.co.goalist.gsc.gen.dtos.MasterSelectionStatusUpsertDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface SelectionStatusMapper extends BaseMapper {

    SelectionStatusMapper SELECTION_STATUS_MAPPER = Mappers.getMapper(SelectionStatusMapper.class);

    MasterSelectionStatusItemsDto toSelectionStatusItemsDto(SelectionStatusDto selectionStatusDto);

    @Mapping(target = "itemName", source = "name")
    @Mapping(target = "flowType", expression = "java(getFlowType(dto.getFlowOrder()))")
    SelectionStatus toEntity(MasterSelectionStatusUpsertDto dto);

    default Integer getFlowType(int flowOrder) {
        return FlowType.fromId(flowOrder).getId();
    }

}
