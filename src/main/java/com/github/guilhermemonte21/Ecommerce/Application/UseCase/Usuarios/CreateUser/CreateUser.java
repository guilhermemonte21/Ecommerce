package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateUser;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.CreateUserRequest;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.UsuarioMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUser implements ICreateUser{
    private final UsuarioGateway gateway;
    private final PasswordEncoder encoder;
    private final UsuarioMapperApl mapperApl;

    public CreateUser(UsuarioGateway gateway, PasswordEncoder encoder, UsuarioMapperApl mapperApl) {
        this.gateway = gateway;
        this.encoder = encoder;
        this.mapperApl = mapperApl;
    }

    @Override
    public Usuarios CreateUser(CreateUserRequest newUser){
        if (gateway.findByEmail(newUser.email()) != null){
            throw new RuntimeException("Usuario já existe");
        }
        Usuarios user = mapperApl.RequestToDomain(newUser);
        user.setSenha(encoder.encode(newUser.senha()));
        user.setTipoUsuario("Comprador");

        return gateway.salvar(user);

    }
}
