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
        UsuariosEntity usuario = new UsuariosEntity();
        usuarios.getNome();
        usuarios.getEmail();
        usuarios.getCpf();
        usuarios.getSenha();
        usuarios.getRole();

        UsuariosEntity Save = JpaRepo.save(usuario);

        Usuarios usuarios1 = new Usuarios();
        usuarios1.setId(Save.getId());
        usuarios1.setNome(Save.getNome());
        usuarios1.setEmail(Save.getEmail());
        usuarios1.setCpf(Save.getCpf());
        usuarios1.setSenha(Save.getSenha());
        usuarios1.setAtivo(Save.getAtivo());
        usuarios1.setRole(Save.getRole());

        return usuarios1;
    }

    @Override
    public Usuarios getById(UUID id) {
        UsuariosEntity usuariosEntity = JpaRepo.findById(id).orElseThrow(() -> new RuntimeException("Usuario Não Encontrado"));

        Usuarios usuarios1 = new Usuarios();
        usuarios1.setId(usuariosEntity.getId());
        usuarios1.setNome(usuariosEntity.getNome());
        usuarios1.setEmail(usuariosEntity.getEmail());
        usuarios1.setCpf(usuariosEntity.getCpf());
        usuarios1.setSenha(usuariosEntity.getSenha());
        usuarios1.setAtivo(usuariosEntity.getAtivo());
        usuarios1.setRole(usuariosEntity.getRole());

        return usuarios1;
    }
}
