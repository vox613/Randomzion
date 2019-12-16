package com.stc21.boot.auction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConditionDto {
    private Long id;
    private String name;
    private Boolean deleted;

    public ConditionDto(String name) {
        this.name = name;
    }
}
