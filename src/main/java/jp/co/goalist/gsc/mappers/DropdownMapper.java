package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.gen.dtos.MDropdownItemsDto;
import jp.co.goalist.gsc.gen.dtos.MTCityItemsDto;
import jp.co.goalist.gsc.gen.dtos.MTPrefectureItemsDto;
import jp.co.goalist.gsc.gen.dtos.MediaDropdownItemsDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DropdownMapper extends BaseMapper {

    DropdownMapper DROPDOWN_MAPPER = Mappers.getMapper(DropdownMapper.class);

    @Mapping(target = "name", source = "fullName")
    MDropdownItemsDto toMDropdownItemsDto(OemAccount oemAccount);

    @Mapping(target = "name", source = "fullName")
    MDropdownItemsDto toMDropdownItemsDto(OperatorAccount operatorAccount);

    @Mapping(target = "name", source = "clientName")
    MDropdownItemsDto toMDropdownItemsDto(OemClientAccount oemClientAccount);

    @Mapping(target = "name", source = "projectName")
    MDropdownItemsDto toMDropdownItemsDto(OperatorProject operatorProject);

    @Mapping(target = "name", source = "projectName")
    MDropdownItemsDto toMDropdownItemsDto(OemProject oemProject);

    @Mapping(target = "name", source = "clientName")
    MDropdownItemsDto toMDropdownItemsDto(OperatorClientAccount operatorClientAccount);

    @Mapping(target = "name", source = "branchName")
    MDropdownItemsDto toMDropdownItemsDto(OperatorBranch operatorBranch);

    @Mapping(target = "name", source = "storeName")
    MDropdownItemsDto toMDropdownItemsDto(OperatorStore operatorStore);

    @Mapping(target = "name", source = "branchName")
    MDropdownItemsDto toMDropdownItemsDto(OemBranch oemBranch);

    @Mapping(target = "name", source = "storeName")
    MDropdownItemsDto toMDropdownItemsDto(OemStore oemStore);

    MDropdownItemsDto toMDropdownItemsDto(OemTeam oemTeam);

    @Mapping(target = "name", source = "statusName")
    MDropdownItemsDto toMDropdownItemsDto(MasterStatus masterStatus);

    MDropdownItemsDto toMDropdownItemsDto(OperatorTeam operatorTeam);

    @Mapping(target = "name", source = "mediaName")
    MediaDropdownItemsDto toMDropdownItemsDto(MasterMedia masterMedia);

    MTPrefectureItemsDto mapToPrefectureDTO(Prefecture entity);

    List<MTCityItemsDto> mapToCitiesDTO(List<City> cities);

    default List<MTCityItemsDto> getCities(final Prefecture prefecture) {
        return mapToCitiesDTO(prefecture.getCities());
    }
}
