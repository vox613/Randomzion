package com.stc21.boot.auction.service;

import com.stc21.boot.auction.dto.UserDto;
import com.stc21.boot.auction.dto.UserRegistrationDto;
import com.stc21.boot.auction.entity.Lot;
import com.stc21.boot.auction.entity.User;
import com.stc21.boot.auction.repository.CityRepository;
import com.stc21.boot.auction.repository.RoleRepository;
import com.stc21.boot.auction.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository, RoleRepository roleRepository, CityRepository cityRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDto> getPaginated(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.unsorted()));

        return userRepository.findByDeletedFalse(pageRequest).map(this::convertToDto);
    }
    @Override
    public Page<UserDto> getPaginatedEvenDeleted(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.unsorted()));

        return userRepository.findAll(pageRequest).map(this::convertToDto);
    }


    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public UserDto findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public UserDto findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public UserDto convertToDto(User user) {
        if (user == null) return null;
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User convertToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }


    /**
     * Опишите док, а то непонятно, что делает метод
     * @param userRegistrationDto
     * @return
     */
    @Override
    public List<String> fieldsWithErrors(UserRegistrationDto userRegistrationDto) {
        List<String> result = new ArrayList<>();

        UserDto existingUser = findByUsername(userRegistrationDto.getUsername());
        if (existingUser != null)
            result.add("username");

        existingUser = findByEmail(userRegistrationDto.getEmail());
        if (existingUser != null)
            result.add("email");

        existingUser = findByPhoneNumber(userRegistrationDto.getPhoneNumber());
        if (existingUser != null) {
            result.add("phoneNumber");
        }

        return result;
    }

    @Override
    public User save(UserRegistrationDto userRegistrationDto) {
        if (userRegistrationDto == null)
            throw new NullPointerException("No userRegistrationDto to save");

        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setPassword(userRegistrationDto.getPassword()); // TODO: to hash
        user.setEmail(userRegistrationDto.getEmail().equals("") ? null : userRegistrationDto.getEmail());
        user.setPhoneNumber(userRegistrationDto.getPhoneNumber().equals("") ? null : userRegistrationDto.getPhoneNumber());
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setRole(roleRepository.getOne(1L));
        user.setCity(userRegistrationDto.getCity());
        user.setWallet(100_000L);
        user.setDeleted(false);

        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void setDeletedTo(long id, boolean newValue) {
        userRepository.updateDeletedTo(id, newValue);
    }
}
