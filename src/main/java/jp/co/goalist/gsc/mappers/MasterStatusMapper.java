package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.dtos.MasterDataStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import jp.co.goalist.gsc.entities.MasterStatus;
import jp.co.goalist.gsc.gen.dtos.MasterDataStatusItemsDto;
import jp.co.goalist.gsc.gen.dtos.MasterDataStatusUpsertDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface MasterStatusMapper {

    MasterStatusMapper MASTER_STATUS_MAPPER = Mappers.getMapper(MasterStatusMapper.class);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "order", source = "dto.order")
    @Mapping(target = "statusName", source = "dto.name")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "oemGroupId", source = "oemGroupId")
    @Mapping(target = "parentId", source = "parentId")
    MasterStatus mapToMasterStatus(MasterDataStatusUpsertDto dto, String parentId, String oemGroupId, String type);

    @Mapping(target = "id", source = "id")
    MasterDataStatusItemsDto toItemsDto(MasterDataStatusDto dto);
}
