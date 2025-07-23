package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.entities.OemAdvertisement;
import jp.co.goalist.gsc.entities.OemApplicant;
import jp.co.goalist.gsc.entities.OperatorAdvertisement;
import jp.co.goalist.gsc.entities.OperatorApplicant;
import jp.co.goalist.gsc.repositories.OemAdvertisementRepository;
import jp.co.goalist.gsc.repositories.OemApplicantRepository;
import jp.co.goalist.gsc.repositories.OperatorAdvertisementRepository;
import jp.co.goalist.gsc.repositories.OperatorApplicantRepository;
import jp.co.goalist.gsc.services.cacheEviction.CacheEvictionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/cache")
public class CacheEvictionController {

    private final OperatorApplicantRepository operatorApplicantRepository;
    private final OemApplicantRepository oemApplicantRepository;
    private final OperatorAdvertisementRepository operatorAdvertisementRepository;
    private final OemAdvertisementRepository oemAdvertisementRepository;
    private final CacheEvictionService cacheEvictionService;

    @DeleteMapping("/operator-applicant")
    public ResponseEntity<Void> evictOperatorApplicantsByProjectId(@RequestBody List<String> projectIds) {
        log.info("_evictOperatorApplicantsByProjectId");
        List<OperatorApplicant> applicants = operatorApplicantRepository.findAllApplicantsByProjectIds(projectIds);
        for (OperatorApplicant applicant : applicants) {
            cacheEvictionService.evictOperatorApplicantCache(applicant.getId());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/oem-applicant")
    public ResponseEntity<Void> evictOemApplicantsByProjectId(@RequestBody List<String> projectIds) {
        log.info("_evictOemApplicantsByProjectId");
        List<OemApplicant> applicants = oemApplicantRepository.findAllApplicantsByProjectIds(projectIds);
        for (OemApplicant applicant : applicants) {
            cacheEvictionService.evictOemApplicantCache(applicant.getId());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/operator-advertisement")
    public ResponseEntity<Void> evictOperatorAdvertisementById(@RequestBody List<String> advIds) {
        log.info("_evictOperatorAdvertisementById");
        List<OperatorAdvertisement> advertisements = operatorAdvertisementRepository.findAllById(advIds);
        for (OperatorAdvertisement advertisement : advertisements) {
            cacheEvictionService.evictOperatorAdvertisementCache(advertisement.getId());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/oem-advertisement")
    public ResponseEntity<Void> evictOemAdvertisementById(@RequestBody List<String> advIds) {
        log.info("_evictOemAdvertisementById");
        List<OemAdvertisement> advertisements = oemAdvertisementRepository.findAllById(advIds);
        for (OemAdvertisement advertisement : advertisements) {
            cacheEvictionService.evictOemAdvertisementCache(advertisement.getId());
        }
        return ResponseEntity.ok().build();
    }
}
