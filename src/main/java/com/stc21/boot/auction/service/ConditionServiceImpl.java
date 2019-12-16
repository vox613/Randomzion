package com.stc21.boot.auction.service;

import com.stc21.boot.auction.dto.ConditionDto;
import com.stc21.boot.auction.entity.Condition;
import com.stc21.boot.auction.repository.ConditionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConditionServiceImpl implements ConditionService {

    private ConditionRepository conditionRepository;
    private ModelMapper modelMapper;

    public ConditionServiceImpl(ConditionRepository conditionRepository, ModelMapper modelMapper) {
        this.conditionRepository = conditionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ConditionDto> getAllConditions() {
        return conditionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConditionDto> getAllSorted(Sort sort) {
        return conditionRepository.findByDeletedFalse(sort).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConditionDto> getAllSortedEvenDeleted(Sort sort) {
        return conditionRepository.findAll(sort).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConditionDto convertToDto(Condition condition) {
        if (condition == null) return null;

        return modelMapper.map(condition, ConditionDto.class);
    }

    @Override
    public List<Condition> findAll() {
        return conditionRepository.findAll();
    }

    @Override
    @Transactional
    public void setDeletedTo(long id, boolean newValue) {
        conditionRepository.updateDeletedTo(id, newValue);
    }

    @Override
    public Condition save(ConditionDto conditionDto) {
        if (conditionDto == null)
            throw new NullPointerException("No conditionDto to save");

        Condition condition = modelMapper.map(conditionDto, Condition.class);
        return conditionRepository.saveAndFlush(condition);
    }
}
