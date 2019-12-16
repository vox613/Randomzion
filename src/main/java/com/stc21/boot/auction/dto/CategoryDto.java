package com.stc21.boot.auction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private Boolean deleted;

    public CategoryDto(String name) {
        this.name = name;
    }
}
