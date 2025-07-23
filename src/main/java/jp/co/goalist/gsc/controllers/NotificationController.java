package jp.co.goalist.gsc.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.gen.apis.NotificationsApi;
import jp.co.goalist.gsc.gen.dtos.NotificationDetailsDto;
import jp.co.goalist.gsc.gen.dtos.NotificationListDto;
import jp.co.goalist.gsc.gen.dtos.NotificationUpsertDto;
import jp.co.goalist.gsc.gen.dtos.SelectedIds;
import jp.co.goalist.gsc.services.NotificationService;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class NotificationController implements NotificationsApi {

    private final NotificationService notificationService;

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<Void> createNewNotification(NotificationUpsertDto notificationUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        log.info("createNewNotification, account {}", account.getRole());

        notificationService.createNewNotification(notificationUpsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<Void> deleteSelectedNotifications(SelectedIds selectedIds) {
        Account account = GeneralUtils.getCurrentUser();
        log.info("deleteSelectedNotifications, account {}", account.getRole());

        notificationService.deleteSelectedNotifications(selectedIds);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<Void> editNewNotification(String id, NotificationUpsertDto notificationUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        log.info("editNewNotification, account {}", account.getRole());

        notificationService.editNewNotification(id, notificationUpsertDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<NotificationDetailsDto> getNotificationDetails(String id) {
        Account account = GeneralUtils.getCurrentUser();
        log.info("getNotificationDetails, account {}", account.getRole());

        return ResponseEntity.ok(notificationService.getNotificationDetails(id));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'OEM')")
    public ResponseEntity<NotificationListDto> getNotificationList(Integer pageNumber,
                                                                   Integer pageSize,
                                                                   String searchInput) {
        Account account = GeneralUtils.getCurrentUser();
        log.info("getNotificationList, account {}", account.getRole());

        return ResponseEntity.ok(notificationService.getNotificationList(pageNumber, pageSize, searchInput, account));
    }

}
