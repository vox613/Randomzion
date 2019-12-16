package com.stc21.boot.auction.service;

import com.stc21.boot.auction.dto.CityDto;
import com.stc21.boot.auction.entity.City;
import com.stc21.boot.auction.repository.CityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;

    public CityServiceImpl(CityRepository cityRepository, ModelMapper modelMapper) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public List<CityDto> getAllSorted(Sort sort) {
        return cityRepository.findByDeletedFalse(sort).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CityDto> getAllSortedEvenDeleted(Sort sort) {
        return cityRepository.findAll(sort).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CityDto convertToDto(City city) {
        if (city == null) return null;

        return modelMapper.map(city, CityDto.class);
    }

    @Override
    @Transactional
    public void setDeletedTo(long id, boolean newValue) {
        cityRepository.updateDeletedTo(id, newValue);
    }

    @Override
    public City save(CityDto cityDto) {
        if (cityDto == null)
            throw new NullPointerException("No cityDto to save");

        City city = modelMapper.map(cityDto, City.class);
        return cityRepository.saveAndFlush(city);
    }
}
