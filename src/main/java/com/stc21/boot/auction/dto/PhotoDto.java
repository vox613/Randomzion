package com.stc21.boot.auction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhotoDto {
    private Long id;
    private LotDto lot;
    private String url;
    private Boolean deleted;
}
