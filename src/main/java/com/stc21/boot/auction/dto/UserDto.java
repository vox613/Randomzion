package com.stc21.boot.auction.dto;

import com.stc21.boot.auction.entity.Role;
import com.stc21.boot.auction.entity.City;
import com.stc21.boot.auction.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
    private City city;
    private Boolean deleted;
    private Long wallet;

    public UserDto(User user) {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.city = user.getCity();
        this.role = user.getRole();
        this.deleted = user.getDeleted();
        this.wallet = user.getWallet();
    }
}
