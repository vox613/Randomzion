package com.stc21.boot.auction.service;

import com.stc21.boot.auction.dto.RoleDto;
import com.stc21.boot.auction.entity.Role;

import java.util.List;

public interface RoleService {
    List<RoleDto> getAllRoles();
    RoleDto convertToDto(Role role);
}
