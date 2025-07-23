package jp.co.goalist.gsc.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import jp.co.goalist.gsc.services.clients.dtos.EmailSendingRequestDto;

@FeignClient(value = "gsc-be-service", url = "${gsc-be-core.service-url}")
public interface GscServiceClient {

    @PostMapping(value = "/emails/sending")
    void sendMail(@RequestHeader HttpHeaders headers, @RequestBody EmailSendingRequestDto dto);
}
