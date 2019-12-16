package com.stc21.boot.auction.service;

import com.stc21.boot.auction.dto.CategoryDto;
import com.stc21.boot.auction.entity.Category;
import com.stc21.boot.auction.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getAllSorted(Sort sort) {
        return categoryRepository.findByDeletedFalse(sort).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getAllSortedEvenDeleted(Sort sort) {
        return categoryRepository.findAll(sort).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto convertToDto(Category category) {
        if (category == null) return null;
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public void setDeletedTo(long id, boolean newValue) {
        categoryRepository.updateDeletedTo(id, newValue);
    }

    @Override
    public Category save(CategoryDto categoryDto) {
        if (categoryDto == null)
            throw  new NullPointerException("No categoryDto to save");

        Category category = modelMapper.map(categoryDto, Category.class);
        return categoryRepository.saveAndFlush(category);
    }
}
