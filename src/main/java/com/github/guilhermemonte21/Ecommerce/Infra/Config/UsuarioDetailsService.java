package com.github.guilhermemonte21.Ecommerce.Infra.Config;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.UsuarioMapper;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaUsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final JpaUsuarioRepository repository;

    public UsuarioDetailsService(JpaUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        UsuariosEntity user = repository.findByEmail(email);


        return new UsuarioDetails(user); // ← ISSO É CRÍTICO
    }
}

