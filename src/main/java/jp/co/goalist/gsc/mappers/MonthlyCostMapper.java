package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.gen.dtos.MasterMonthlyCostUpsertDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import jp.co.goalist.gsc.entities.MonthlyCost;
import jp.co.goalist.gsc.gen.dtos.MasterMonthlyCostItemsDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface MonthlyCostMapper {

    MonthlyCostMapper MONTHLY_COST_MAPPER = Mappers.getMapper(MonthlyCostMapper.class);

    @Mapping(target = "mediaId", source = "media.id")
    MasterMonthlyCostItemsDto toItemsDto(MonthlyCost entity);

    @Mapping(target = "media", ignore = true)
    @Mapping(target = "parentId", source = "parentId")
    @Mapping(target = "oemGroupId", source = "oemGroupId")
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "amount", source = "dto.amount")
    MonthlyCost toMonthlyCost(MasterMonthlyCostUpsertDto dto, String parentId, String oemGroupId);
}
