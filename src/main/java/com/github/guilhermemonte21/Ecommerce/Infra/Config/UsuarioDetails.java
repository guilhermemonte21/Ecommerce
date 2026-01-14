package com.github.guilhermemonte21.Ecommerce.Infra.Config;

import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UsuarioDetails implements UserDetails {

    private final UsuariosEntity user;

    public UsuarioDetails(UsuariosEntity user) {
        this.user = user;
    }

    public UsuariosEntity getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getSenha();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // ou UUID, mas seja consistente
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}