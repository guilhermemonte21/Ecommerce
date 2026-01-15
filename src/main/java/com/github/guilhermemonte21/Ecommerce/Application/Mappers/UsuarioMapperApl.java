package com.github.guilhermemonte21.Ecommerce.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.CreateUserRequest;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapperApl {
    public Usuarios RequestToDomain(CreateUserRequest user){
        Usuarios newUser = new Usuarios();
        newUser.setNome(user.nome());
        newUser.setEmail(user.email());
        newUser.setAtivo(true);
        newUser.setCpf(user.cpf());
        return newUser;
    }
}
