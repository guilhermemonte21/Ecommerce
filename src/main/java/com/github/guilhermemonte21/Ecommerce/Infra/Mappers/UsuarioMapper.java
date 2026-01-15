package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Enum.TipoUsuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuarios toDomain(UsuariosEntity entity){
        if(entity == null){
            return null;
        }
        Usuarios NewUser = new Usuarios();
        NewUser.setId(entity.getId());
        NewUser.setCpf(entity.getCpf());
        NewUser.setEmail(entity.getEmail());

        NewUser.setNome(entity.getNome());
        NewUser.setAtivo(entity.getAtivo());
        NewUser.setSenha(entity.getSenha());
        if(entity.getTipoUsuario() == TipoUsuario.Comprador){
            NewUser.setTipoUsuario("Comprador");
        }
        if(entity.getTipoUsuario() == TipoUsuario.Vendedor){
            NewUser.setTipoUsuario("Vendedor");
        }
        NewUser.setGatewayAccountId(entity.getGatewayAccountId());
        return NewUser;
    }

    public UsuariosEntity toEntity(Usuarios usuarios){
        if (usuarios == null){
            return null;
        }
        UsuariosEntity entity = new UsuariosEntity();
        entity.setId(usuarios.getId());
        entity.setNome(usuarios.getNome());
        entity.setCpf(usuarios.getCpf());
        entity.setSenha(usuarios.getSenha());
        entity.setEmail(usuarios.getEmail());
        entity.setAtivo(usuarios.getAtivo());
        if(usuarios.getTipoUsuario() == "Comprador"){
        entity.setTipoUsuario(TipoUsuario.Comprador);
        }
        if (usuarios.getTipoUsuario() == "Vendedor"){
            entity.setTipoUsuario(TipoUsuario.Vendedor);
        }
        entity.setGatewayAccountId(usuarios.getGatewayAccountId());
        return entity;
    }
}
