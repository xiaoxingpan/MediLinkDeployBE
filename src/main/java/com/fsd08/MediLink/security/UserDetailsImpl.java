package com.fsd08.MediLink.security;

import com.fsd08.MediLink.MediLinkApplication;
import com.fsd08.MediLink.entity.User;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;
@Data
public class UserDetailsImpl implements UserDetails {
    private static final Logger logger = LoggerFactory.getLogger(MediLinkApplication.class);

    private String username;
    private String password;
    private boolean isEnabled;
    private List<GrantedAuthority> authorities;

    public UserDetailsImpl(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.isEnabled =user.isEnabled();

        this.authorities = Arrays.stream(user.getAuthority()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
