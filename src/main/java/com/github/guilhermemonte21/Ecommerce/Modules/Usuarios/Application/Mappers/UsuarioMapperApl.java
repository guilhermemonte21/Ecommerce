package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.DTO.Usuarios.CreateUserRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.DTO.Usuarios.UsuarioResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapperApl {

    public Usuarios requestToDomain(CreateUserRequest user) {
        Usuarios newUser = new Usuarios();
        newUser.setNome(user.nome());
        newUser.setEmail(user.email());
        newUser.setAtivo(true);
        newUser.setCpf(user.cpf());
        return newUser;
    }

    public UsuarioResponse toResponse(Usuarios user) {
        return new UsuarioResponse(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getCpf(),
                user.getAtivo(),
                user.getTipoUsuario()
        );
    }
}
