package jp.co.goalist.gsc.entities;

import java.io.Serializable;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OemAdvertisementLinkingId implements Serializable {

    private String projectId;

    private String advertisementId;

}
