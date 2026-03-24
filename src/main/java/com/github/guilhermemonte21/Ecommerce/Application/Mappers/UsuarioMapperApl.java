package com.github.guilhermemonte21.Ecommerce.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.CreateUserRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.UsuarioResponse;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
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
