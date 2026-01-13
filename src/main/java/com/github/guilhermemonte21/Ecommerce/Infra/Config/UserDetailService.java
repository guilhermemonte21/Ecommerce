package com.github.guilhermemonte21.Ecommerce.Infra.Config;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.UsuarioMapper;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaUsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailService {

    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final UsuarioMapper mapper;

    public UserDetailService(JpaUsuarioRepository jpaUsuarioRepository, UsuarioMapper mapper) {
        this.jpaUsuarioRepository = jpaUsuarioRepository;
        this.mapper = mapper;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuarios user = mapper.toDomain(jpaUsuarioRepository.findByEmail(username));
        if(user == null){
            throw new UsernameNotFoundException("Usuario não encontrado");
        }
        return User.builder()
                .username(user.getEmail())
                .password(user.getSenha())
                .roles(user.getRole().toString())
                .build();
    }
}
