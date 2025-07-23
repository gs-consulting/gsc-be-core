package jp.co.goalist.gsc.services;

import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.MimeTypeUtils;

import jp.co.goalist.gsc.services.clients.GscServiceClient;
import jp.co.goalist.gsc.services.events.MailRegisterEvent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    private final GscServiceClient gscServiceClient;

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", MimeTypeUtils.APPLICATION_JSON_VALUE);
        return headers;
    }

    @Async("taskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMailRegisterEvent(MailRegisterEvent mailEvent) {
        HttpHeaders headers = getDefaultHeaders();
        headers.set("Authorization", "Bearer " + mailEvent.getDto().getJwt());
        gscServiceClient.sendMail(headers, mailEvent.getDto());
    }
}
