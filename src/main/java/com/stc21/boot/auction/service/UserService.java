package com.stc21.boot.auction.service;

import com.stc21.boot.auction.dto.UserDto;
import com.stc21.boot.auction.dto.UserRegistrationDto;
import com.stc21.boot.auction.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();
    Page<UserDto> getPaginated(Pageable pageable);
    Page<UserDto> getPaginatedEvenDeleted(Pageable pageable);
    UserDto findById(Long id);
    UserDto findByUsername(String username);
    UserDto findByEmail(String email);
    UserDto findByPhoneNumber(String phoneNumber);
    UserDto convertToDto(User user);
    User convertToEntity(UserDto userDto);

    List<String> fieldsWithErrors(UserRegistrationDto userRegistrationDto);
    User save(UserRegistrationDto userRegistrationDto);

    void setDeletedTo(long id, boolean newValue);
}
