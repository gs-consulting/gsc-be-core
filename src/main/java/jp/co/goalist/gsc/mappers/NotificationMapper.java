package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.enums.PublishingStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import jp.co.goalist.gsc.entities.Notification;
import jp.co.goalist.gsc.gen.dtos.NotificationDetailsDto;
import jp.co.goalist.gsc.gen.dtos.NotificationItemsDto;
import jp.co.goalist.gsc.gen.dtos.NotificationUpsertDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface NotificationMapper extends BaseMapper {

    NotificationMapper NOTIFICATION_MAPPER = Mappers.getMapper(NotificationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "postingStartDate", source = "postingStartDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "postingEndDate", source = "postingEndDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "status", ignore = true)
    Notification createNew(NotificationUpsertDto form);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateExisting(NotificationUpsertDto form, @MappingTarget Notification entity);

    @Mapping(target = "isDeletable", expression = "java(checkIsDeletable(entity))")
    @Mapping(target = "postingStartDate", source = "postingStartDate", dateFormat = "yyyy/MM/dd")
    @Mapping(target = "postingEndDate", source = "postingEndDate", dateFormat = "yyyy/MM/dd")
    NotificationItemsDto toNotificationItemsDto(Notification entity);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isDeletable", ignore = true)
    @Mapping(target = "postingStartDate", source = "postingStartDate", dateFormat = "yyyy/MM/dd")
    @Mapping(target = "postingEndDate", source = "postingEndDate", dateFormat = "yyyy/MM/dd")
    NotificationItemsDto toNotificationItemsDtoForClient(Notification entity);

    NotificationDetailsDto toNotificationDetailsDto(Notification notification);

    default boolean checkIsDeletable(Notification notification) {
        return notification.getStatus().equals(PublishingStatus.PRIVATE.getId());
    }

}
