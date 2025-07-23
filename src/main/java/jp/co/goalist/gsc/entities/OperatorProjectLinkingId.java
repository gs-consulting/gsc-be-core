package jp.co.goalist.gsc.entities;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OperatorProjectLinkingId implements Serializable {

    private String project1Id;

    private String project2Id;

}
