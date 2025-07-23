package jp.co.goalist.gsc.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectionStatusDto {

    public Long id;

    public String name;

    public Integer displayOrder;

    public Integer flowOrder;

    public Boolean isDeletable;
}
