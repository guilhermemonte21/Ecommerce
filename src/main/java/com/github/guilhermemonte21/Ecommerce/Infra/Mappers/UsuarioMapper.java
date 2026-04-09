package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Enum.TipoUsuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuarios toDomain(UsuariosEntity entity) {
        if (entity == null) {
            return null;
        }
        Usuarios user = new Usuarios();
        user.setId(entity.getId());
        user.setCpf(entity.getCpf());
        user.setEmail(entity.getEmail());
        user.setNome(entity.getNome());
        user.setAtivo(entity.getAtivo());
        user.setSenha(entity.getSenha());
        if (entity.getTipoUsuario() != null) {
            user.setTipoUsuario(entity.getTipoUsuario().name());
        }
        user.setStripeAccountId(entity.getStripeAccountId());
        return user;
    }

    public UsuariosEntity toEntity(Usuarios usuarios) {
        if (usuarios == null) {
            return null;
        }
        UsuariosEntity entity = new UsuariosEntity();
        entity.setId(usuarios.getId());
        entity.setNome(usuarios.getNome());
        entity.setCpf(usuarios.getCpf());
        entity.setSenha(usuarios.getSenha());
        entity.setEmail(usuarios.getEmail());
        entity.setAtivo(usuarios.getAtivo());
        if (usuarios.getTipoUsuario() != null) {
            try {
                entity.setTipoUsuario(TipoUsuario.valueOf(usuarios.getTipoUsuario()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Tipo de usuário desconhecido: " + usuarios.getTipoUsuario());
            }
        }
        entity.setStripeAccountId(usuarios.getStripeAccountId());
        return entity;
    }
}
