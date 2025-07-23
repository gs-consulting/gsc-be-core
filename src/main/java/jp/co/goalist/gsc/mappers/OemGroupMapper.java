package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.entities.OemGroup;
import jp.co.goalist.gsc.gen.dtos.OemGroupItemsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface OemGroupMapper extends BaseMapper {

    OemGroupMapper OEM_GROUP_MAPPER = Mappers.getMapper(OemGroupMapper.class);

    @Mapping(target = "oemName", source = "oemGroupName")
    OemGroupItemsDto toOemGroupNameItemsDto(OemGroup oemGroup);

}
