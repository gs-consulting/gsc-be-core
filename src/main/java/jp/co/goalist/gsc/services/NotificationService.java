package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.entities.Notification;
import jp.co.goalist.gsc.enums.PublishingStatus;
import jp.co.goalist.gsc.enums.Role;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.NotificationRepository;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static jp.co.goalist.gsc.mappers.NotificationMapper.NOTIFICATION_MAPPER;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification getExistingByIdAndStatus(String id, String status) {
        Optional<Notification> optionalOne = notificationRepository.findByIdAndStatus(id, status);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.NOTIFICATION.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    @Transactional
    public void createNewNotification(NotificationUpsertDto notificationUpsertDto) {
        PublishingStatus status = PublishingStatus.valueOf(notificationUpsertDto.getStatus());
        Notification notification = NOTIFICATION_MAPPER.createNew(notificationUpsertDto);
        notification.setStatus(status.getId());
        notificationRepository.saveAndFlush(notification);
    }

    @Transactional
    public void editNewNotification(String id, NotificationUpsertDto notificationUpsertDto) {
        Notification notification = getExistingByIdAndStatus(id, null);
        PublishingStatus status = PublishingStatus.valueOf(notificationUpsertDto.getStatus());
        NOTIFICATION_MAPPER.updateExisting(notificationUpsertDto, notification);
        notification.setStatus(status.getId());
        notificationRepository.saveAndFlush(notification);
    }

    @Transactional
    public void deleteSelectedNotifications(SelectedIds selectedIds) {
        List<String> ids = selectedIds.getSelectedIds();
        List<Notification> invalidItems = notificationRepository.checkInUseStatus(ids);
        if (!invalidItems.isEmpty()) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DELETION.getStatusCode())
                    .message(String.format(ErrorMessage.INVALID_DELETION.getMessage(),
                            TargetName.NOTIFICATION.getTargetName(),
                            invalidItems.getFirst().getTitle()))
                    .build());
        }

        notificationRepository.delete(ids);
    }

    public NotificationDetailsDto getNotificationDetails(String id) {
        Notification notification = getExistingByIdAndStatus(id, null);
        return NOTIFICATION_MAPPER.toNotificationDetailsDto(notification);
    }

    public NotificationListDto getNotificationList(Integer pageNumber, Integer pageSize, String searchInput, Account account) {
        Page<Notification> notificationList;
        NotificationListDto notificationListDto;
        if (account.getRole().equals(Role.CLIENT.getId())) {
            notificationList = notificationRepository.getNotificationList(
                    GeneralUtils.wrapToLike(searchInput),
                    Pageable.unpaged()
            );
            notificationListDto = NotificationListDto.builder()
                    .page(notificationList.getNumber() + 1)
                    .limit(notificationList.getSize())
                    .total(notificationList.getTotalElements())
                    .items(notificationList.getContent()
                    .stream().map(NOTIFICATION_MAPPER::toNotificationItemsDtoForClient).collect(Collectors.toList()))
                    .build();
        } else {
            notificationList = notificationRepository.getNotificationList(
                    GeneralUtils.wrapToLike(searchInput),
                    PageRequest.of(pageNumber - 1, pageSize)
            );
            notificationListDto = NotificationListDto.builder()
                    .page(notificationList.getNumber() + 1)
                    .limit(notificationList.getSize())
                    .total(notificationList.getTotalElements())
                    .items(notificationList.getContent()
                            .stream().map(NOTIFICATION_MAPPER::toNotificationItemsDto).collect(Collectors.toList()))
                    .build();
        }
        return notificationListDto;
    }

    public List<Notification> findAllPublicNotifications() {
        return notificationRepository.findAllPublicNotifications();
    }
}
