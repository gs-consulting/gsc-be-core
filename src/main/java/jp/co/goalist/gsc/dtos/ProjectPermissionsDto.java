package jp.co.goalist.gsc.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermissionsDto {

    private String id;

    private String projectNo;

    private String projectName;

    private String permission;
}
