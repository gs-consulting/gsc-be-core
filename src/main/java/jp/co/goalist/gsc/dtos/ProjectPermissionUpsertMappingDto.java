package jp.co.goalist.gsc.dtos;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermissionUpsertMappingDto {

    private String id;

    private String permission;
}
