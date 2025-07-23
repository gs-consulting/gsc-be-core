package jp.co.goalist.gsc.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.entities.OemTemplate;
import jp.co.goalist.gsc.entities.OperatorTemplate;
import jp.co.goalist.gsc.gen.dtos.MTemplateItemsDto;
import jp.co.goalist.gsc.gen.dtos.TemplateDetailsDto;
import jp.co.goalist.gsc.gen.dtos.TemplateItemsDto;
import jp.co.goalist.gsc.gen.dtos.TemplateUpsertDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TemplateMapper {

    TemplateMapper TEMPLATE_MAPPER = Mappers.getMapper(TemplateMapper.class);

    @Mapping(target = "templateName", source = "name")
    OperatorTemplate toOperatorTemplate(TemplateUpsertDto dto);

    @Mapping(target = "templateName", source = "name")
    OemTemplate toOemTemplate(TemplateUpsertDto dto);

    @Mapping(target = "templateName", source = "name")
    void updateEntity(TemplateUpsertDto dto, @MappingTarget OperatorTemplate entity);

    @Mapping(target = "templateName", source = "name")
    void updateEntity(TemplateUpsertDto dto, @MappingTarget OemTemplate entity);

    @Mapping(target = "name", source = "templateName")
    TemplateDetailsDto toOpDetailsDto(OperatorTemplate opTemplate);

    @Mapping(target = "name", source = "templateName")
    TemplateDetailsDto toOemDetailsDto(OemTemplate oemTemplate);

    @Mapping(target = "name", source = "templateName")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    TemplateItemsDto toOpItemsDto(OperatorTemplate opTemplate);

    @Mapping(target = "name", source = "templateName")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    TemplateItemsDto toOemItemsDto(OemTemplate oemTemplate);

    @Mapping(target = "name", source = "templateName")
    MTemplateItemsDto toMTemplateItemsDto(OperatorTemplate opTemplate);

    @Mapping(target = "name", source = "templateName")
    MTemplateItemsDto toMTemplateItemsDto(OemTemplate oemTemplate);
}
