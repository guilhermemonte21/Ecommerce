package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuarios toDomain(UsuariosEntity entity){
        Usuarios NewUser = new Usuarios();
        NewUser.setId(entity.getId());
        NewUser.setCpf(entity.getCpf());
        NewUser.setEmail(entity.getEmail());
        NewUser.setRole(entity.getRole());
        NewUser.setNome(entity.getNome());
        NewUser.setAtivo(entity.getAtivo());
        NewUser.setSenha(entity.getSenha());
        return NewUser;
    }

    public UsuariosEntity toEntity(Usuarios usuarios){
        UsuariosEntity entity = new UsuariosEntity();
        entity.setNome(usuarios.getNome());
        entity.setCpf(usuarios.getCpf());
        entity.setSenha(usuarios.getSenha());
        entity.setEmail(usuarios.getEmail());
        return entity;
    }
}
