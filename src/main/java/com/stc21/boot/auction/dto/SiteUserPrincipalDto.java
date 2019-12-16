package com.stc21.boot.auction.dto;

import com.stc21.boot.auction.entity.Role;
import com.stc21.boot.auction.entity.User;
import org.hibernate.resource.beans.internal.BeansMessageLogger_$logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// необходим для аутентификации/авторизации через базу данных в Spring Security
// используется в security.SiteUserDetailsService
public class SiteUserPrincipalDto implements UserDetails {

    private final String username;
    private final String password;
    private final Role role;
    private final Boolean deleted;

    public SiteUserPrincipalDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.deleted = user.getDeleted();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.deleted;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.deleted;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.deleted;
    }

    @Override
    public boolean isEnabled() {
        return !this.deleted;
    }
}
