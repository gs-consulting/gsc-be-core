package jp.co.goalist.gsc.services.clients.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Getter
@Builder
public class EmailSendingRequestDto {

    private String email;

    private String sendingType;

    private String templateName;

    @JsonIgnore
    private String jwt;
}
