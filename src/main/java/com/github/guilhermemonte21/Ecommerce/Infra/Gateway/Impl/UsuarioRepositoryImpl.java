package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.JpaRepository.JpaUsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsuarioRepositoryImpl implements UsuarioGateway {

    private final JpaUsuarioRepository JpaRepo;

    public UsuarioRepositoryImpl(JpaUsuarioRepository jpaRepo) {
        JpaRepo = jpaRepo;
    }

    @Override
    public Usuarios salvar(Usuarios usuarios) {
        UsuariosEntity usuario = new UsuariosEntity(usuarios.getNome(),usuarios.getEmail(),usuarios.getCpf(), usuarios.getSenha());

        UsuariosEntity Save = JpaRepo.save(usuario);

        return new Usuarios(Save.getNome(), Save.getEmail(), Save.getCpf(), Save.getSenha());
    }

    @Override
    public Usuarios getById(UUID id) {
        UsuariosEntity UserById = JpaRepo.findById(id).orElseThrow(() -> new RuntimeException("Usuario não Encontrado"));

        Usuarios usuario = new Usuarios(UserById.getNome(), UserById.getEmail(), UserById.getCpf(), UserById.getSenha());

        return usuario;
    }
}
