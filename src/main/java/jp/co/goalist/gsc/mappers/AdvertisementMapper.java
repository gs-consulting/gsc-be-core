package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.entities.OemAdvertisement;
import jp.co.goalist.gsc.entities.OperatorAdvertisement;
import jp.co.goalist.gsc.gen.dtos.ProjectAdvertItemsDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AdvertisementMapper {

    AdvertisementMapper ADVERTISEMENT_MAPPER = Mappers.getMapper(AdvertisementMapper.class);

    @Mapping(target = "advertisementName", source = "name")
    @Mapping(target = "publishingStartDate", source = "startDate")
    @Mapping(target = "publishingEndDate", source = "endDate")
    @Mapping(target = "mediaName", source = "masterMedia.mediaName")
    ProjectAdvertItemsDto toProjectAdvertisements(OperatorAdvertisement advertisement);

    @Mapping(target = "advertisementName", source = "name")
    @Mapping(target = "publishingStartDate", source = "startDate")
    @Mapping(target = "publishingEndDate", source = "endDate")
    @Mapping(target = "mediaName", source = "masterMedia.mediaName")
    ProjectAdvertItemsDto toProjectAdvertisements(OemAdvertisement advertisement);
}
