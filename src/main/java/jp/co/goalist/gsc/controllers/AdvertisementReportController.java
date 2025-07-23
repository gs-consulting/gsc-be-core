package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import jp.co.goalist.gsc.gen.apis.AdvertisementReportsApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdvertisementReportController implements AdvertisementReportsApi {
    private final MediaReportService mediaReportService;

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<MediaReportBeforeJoinDto> getMediaCostBeforeJoin(MediaReportSearchBoxDto mediaReportSearchBoxDto) {
        log.info("getMediaCostBeforeJoin");
        return ResponseEntity.ok(mediaReportService.getMediaCostBeforeJoin(mediaReportSearchBoxDto));
    }

    @Override
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<MediaReportAfterJoinDto> getMediaCostAfterJoin(MediaReportSearchBoxDto mediaReportSearchBoxDto) {
        log.info("getMediaCostBeforeJoin");
        return ResponseEntity.ok(mediaReportService.getMediaCostAfterJoin(mediaReportSearchBoxDto));
    }
}
