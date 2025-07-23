package jp.co.goalist.gsc.entities;

import java.io.Serializable;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OperatorAdvertisementLinkingId implements Serializable {

    private String projectId;

    private String advertisementId;

}
