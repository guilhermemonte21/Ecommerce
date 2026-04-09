package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Infra.Mappers.UsuarioMapper;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Infra.Persistence.Entity.Data.UsuariosEntity;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Infra.Persistence.JpaRepository.JpaUsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioRepositoryImpl implements UsuarioGateway {

    private final JpaUsuarioRepository jpaRepo;
    private final UsuarioMapper mapper;

    public UsuarioRepositoryImpl(JpaUsuarioRepository jpaRepo, UsuarioMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Usuarios salvar(Usuarios usuarios) {
        UsuariosEntity usuariosEntity = mapper.toEntity(usuarios);
        UsuariosEntity salvo = jpaRepo.save(usuariosEntity);
        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<Usuarios> getById(UUID id) {
        return jpaRepo.findById(id).map(mapper::toDomain);
    }

    @Override
    public Usuarios findByEmail(String email) {
        UsuariosEntity entity = jpaRepo.findByEmail(email);
        return mapper.toDomain(entity);
    }
}
