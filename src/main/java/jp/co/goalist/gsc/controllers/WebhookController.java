package jp.co.goalist.gsc.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import jp.co.goalist.gsc.gen.apis.WebhookApi;
import jp.co.goalist.gsc.gen.dtos.ExternalApplicantCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebhookController implements WebhookApi {

    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<Void> createNewApplicantWebhook(
            @Valid ExternalApplicantCreateDto externalApplicantCreateDto) {
        
        try {
            // debug
            String dtoString = objectMapper.writeValueAsString(externalApplicantCreateDto);
            log.info("Received ExternalApplicantCreateDto: {}", dtoString);
        } catch (Exception e) {
            log.error("Error serializing ExternalApplicantCreateDto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
