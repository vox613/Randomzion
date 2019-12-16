package com.stc21.boot.auction.service;

import com.stc21.boot.auction.dto.CityDto;
import com.stc21.boot.auction.entity.City;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CityService {
    List<CityDto> getAllSorted(Sort sort);
    List<CityDto> getAllSortedEvenDeleted(Sort sort);
    CityDto convertToDto(City city);
    List<City> getAllCities();

    void setDeletedTo(long id, boolean newValue);
    City save(CityDto cityDto);
}
