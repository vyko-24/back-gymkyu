package com.camila.gymkyu.security.entity;

import com.camila.gymkyu.models.usuarios.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {
    private String email;
    private String password;
    private boolean blocked;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String email, String password, boolean blocked, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.password = password;
        this.blocked = blocked;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public static UserDetails build(Usuario user) {
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(user.getRole().getNombre()));

        return new UserDetailsImpl(
                user.getEmail(),
                user.getContrasena(),
                user.getStatus(),
                user.getStatus(),
                authorities
        );
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
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return blocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
