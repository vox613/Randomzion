package com.stc21.boot.auction.dto;

import com.stc21.boot.auction.entity.Category;
import com.stc21.boot.auction.entity.City;
import com.stc21.boot.auction.entity.Condition;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class LotDto {
    private Long id;

    @NotNull
    @Size(min = 5, max = 50)
    private String name;

    private LocalDateTime creationTime;
    private LocalDateTime lastModTime;

    @NotNull
    @Size(min = 5, max = 150)
    private String description;

    private Category category;
    private Condition condition;
    private City city;

    @NotNull
    @Range(min = 0, max = 50000)
    private Long currentPrice;
    @NotNull
    @Range(min = 0, max = 50000)
    private Long maxPrice;
    @NotNull
    @Range(min = 0, max = 50000)
    private Long minPrice;

    private Long stepPrice;

    private UserDto userDto;

    private List<String> photoUrls = new ArrayList<>();

    private Boolean deleted = false;
}
