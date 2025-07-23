package jp.co.goalist.gsc.services.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import jp.co.goalist.gsc.services.clients.dtos.EmailSendingRequestDto;

@Getter
public class MailRegisterEvent extends ApplicationEvent {

    private EmailSendingRequestDto dto;

    public MailRegisterEvent(Object source, EmailSendingRequestDto dto) {
        super(source);
        this.dto = dto;
    }
}
