package jp.co.goalist.gsc.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistInfoDto {

    private String id;

    private String fullName;

    private String telOrEmail;
}
