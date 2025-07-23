package jp.co.goalist.gsc.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OemClientAccountItemsDto {

    private String id;

    private String oemGroupId;

    private String clientName;

    private String managerNames;

    private String branchIds;

    private String branchNames;

    private String storeNames;

    private Boolean isMember;

    private String resetTokenString;

    private Timestamp tokenExpirationDate;

    private Timestamp createdAt;
}
