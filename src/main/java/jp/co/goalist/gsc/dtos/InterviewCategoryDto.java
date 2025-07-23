package jp.co.goalist.gsc.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewCategoryDto {

    private Long id;

    private String name;

    private Integer order;

    private Boolean isDeletable;
}
