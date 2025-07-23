package jp.co.goalist.gsc.entities;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OemProjectLinkingId implements Serializable {

    private String project1Id;

    private String project2Id;

}