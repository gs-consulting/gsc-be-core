package jp.co.goalist.gsc.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import jp.co.goalist.gsc.gen.apis.DashboardApi;
import jp.co.goalist.gsc.gen.dtos.DApplicantItemsDto;
import jp.co.goalist.gsc.gen.dtos.DChatItemsDto;
import jp.co.goalist.gsc.gen.dtos.DInterviewItemsDto;
import jp.co.goalist.gsc.gen.dtos.DNotificationDetailsDto;
import jp.co.goalist.gsc.gen.dtos.DNotificationItemsDto;
import jp.co.goalist.gsc.gen.dtos.DProjectItemsDto;
import jp.co.goalist.gsc.gen.dtos.StatisticsListDto;
import jp.co.goalist.gsc.services.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DashboardController implements DashboardApi {
    
    private final DashboardService dashboardService;

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<StatisticsListDto> getApplicationStatistics() {
        log.info("getApplicationStatistics");
        return ResponseEntity.ok(dashboardService.getApplicationStatistics());
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<DChatItemsDto>> getChats() {
        log.info("getChats");
        return ResponseEntity.ok(dashboardService.getChats());
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<DNotificationDetailsDto> getDNotificationDetails(String id) {
        log.info("getDNotificationDetails");
        return ResponseEntity.ok(dashboardService.getDNotificationDetails(id));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<DInterviewItemsDto>> getInterviews() {
        log.info("getInterviews");
        return ResponseEntity.ok(dashboardService.getInterviews());
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<DApplicantItemsDto>> getNewApplicants() {
        log.info("getNewApplicants");
        return ResponseEntity.ok(dashboardService.getNewApplicants());
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<DNotificationItemsDto>> getNotifications() {
        log.info("getNotifications");
        return ResponseEntity.ok(dashboardService.getNotifications());
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<DProjectItemsDto>> getProjects() {
        log.info("getProjects");
        return ResponseEntity.ok(dashboardService.getProjects());
    }

}
