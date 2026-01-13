package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.UsuarioMapper;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaUsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioRepositoryImpl implements UsuarioGateway {

    private final JpaUsuarioRepository JpaRepo;
    private final UsuarioMapper mapper;
    private final PasswordEncoder encoder;

    public UsuarioRepositoryImpl(JpaUsuarioRepository jpaRepo, UsuarioMapper mapper, PasswordEncoder encoder) {
        JpaRepo = jpaRepo;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @Override
    public Usuarios salvar(Usuarios usuarios) {
        UsuariosEntity usuariosEntity = mapper.toEntity(usuarios);

        UsuariosEntity Save = JpaRepo.save(usuariosEntity);

        Usuarios usuarios1 = mapper.toDomain(Save);

        return usuarios1;
    }

    @Override
    public Optional<Usuarios> getById(UUID id) {
        Optional<Usuarios> usuariosById = JpaRepo.findById(id).map(mapper::toDomain);
        return usuariosById;
    }

    public String Login(String email, String senha){
        Usuarios user = mapper.toDomain(JpaRepo.findByEmail(email));
        if (!user.getSenha().matches(encoder.encode(senha))){
           throw new RuntimeException("Senha Invalida");
        }
        return "Logado";
    }
    @Override
    public Usuarios findByEmail(String email){
        Usuarios user = mapper.toDomain(JpaRepo.findByEmail(email));

        return user;
    }
}
