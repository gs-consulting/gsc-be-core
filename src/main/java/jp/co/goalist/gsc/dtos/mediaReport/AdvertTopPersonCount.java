package jp.co.goalist.gsc.dtos.mediaReport;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertTopPersonCount {

    private String id;

    private String name;

    private Long idx;

    private String hexColor;
}
