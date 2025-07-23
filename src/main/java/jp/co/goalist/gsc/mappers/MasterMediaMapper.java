package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.enums.MediaSiteName;
import jp.co.goalist.gsc.gen.dtos.CrawlerMediasDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import jp.co.goalist.gsc.entities.MasterMedia;
import jp.co.goalist.gsc.gen.dtos.MasterMediaDetailsDto;
import jp.co.goalist.gsc.gen.dtos.MasterMediaItemsDto;
import jp.co.goalist.gsc.gen.dtos.MasterMediaUpsertDto;

import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface MasterMediaMapper {

    MasterMediaMapper MASTER_MEDIA_MAPPER = Mappers.getMapper(MasterMediaMapper.class);

    @Mapping(target = "mediaName", source = "name")
    @Mapping(target = "siteName", expression = "java(mapMediaSite(dto))")
    @Mapping(target = "loginId", source = "managementLoginId")
    @Mapping(target = "password", source = "managementPwd")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    MasterMedia toEntity(MasterMediaUpsertDto dto);

    @Mapping(target = "name", source = "mediaName")
    MasterMediaItemsDto toMasterMediaItemsDto(MasterMedia entity);

    @Mapping(target = "name", source = "mediaName")
    @Mapping(target = "managementLoginId", source = "loginId")
    @Mapping(target = "managementPwd", source = "password")
    MasterMediaDetailsDto toMasterMediaDetailsDto(MasterMedia media);

    @Mapping(target = "mediaName", source = "name")
    @Mapping(target = "siteName", expression = "java(mapMediaSite(dto))")
    @Mapping(target = "loginId", source = "managementLoginId")
    @Mapping(target = "password", source = "managementPwd")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(MasterMediaUpsertDto dto, @MappingTarget MasterMedia media);

    @Mapping(target = "mediaId", source = "id")
    CrawlerMediasDto toCrawlerMediasDto(MasterMedia media);

    @Named("mapMediaSite")
    default String mapMediaSite(MasterMediaUpsertDto dto) {
        return Objects.nonNull(dto.getSiteName()) ? MediaSiteName.fromId(dto.getSiteName()).getId() : null;
    }
}
