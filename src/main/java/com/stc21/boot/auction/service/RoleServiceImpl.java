package com.stc21.boot.auction.service;

import com.stc21.boot.auction.dto.RoleDto;
import com.stc21.boot.auction.entity.Role;
import com.stc21.boot.auction.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDto convertToDto(Role role) {
        if (role == null) return null;

        return modelMapper.map(role, RoleDto.class);
    }


}
