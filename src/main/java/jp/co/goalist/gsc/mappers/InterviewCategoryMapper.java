package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.dtos.InterviewCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import jp.co.goalist.gsc.entities.InterviewCategory;
import jp.co.goalist.gsc.gen.dtos.MasterInterviewCategoryItemsDto;
import jp.co.goalist.gsc.gen.dtos.MasterInterviewCategoryUpsertDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface InterviewCategoryMapper {

    InterviewCategoryMapper INTERVIEW_CATEGORY_MAPPER = Mappers.getMapper(InterviewCategoryMapper.class);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "order", source = "dto.order")
    @Mapping(target = "categoryName", source = "dto.name")
    @Mapping(target = "parentId", source = "parentId")
    @Mapping(target = "oemGroupId", source = "oemGroupId")
    InterviewCategory mapToInterviewCategory(MasterInterviewCategoryUpsertDto dto, String parentId, String oemGroupId);

    @Mapping(target = "id", source = "id")
    MasterInterviewCategoryItemsDto toInterviewCategoryItem(InterviewCategoryDto entity);
}
