package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.CreateUser;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.DTO.Usuarios.CreateUserRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.DTO.Usuarios.UsuarioResponse;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioJaExisteException;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Mappers.UsuarioMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateUser implements ICreateUser {

    private static final Logger log = LoggerFactory.getLogger(CreateUser.class);

    private final UsuarioGateway gateway;
    private final PasswordEncoder encoder;
    private final UsuarioMapperApl mapperApl;

    public CreateUser(UsuarioGateway gateway, PasswordEncoder encoder, UsuarioMapperApl mapperApl) {
        this.gateway = gateway;
        this.encoder = encoder;
        this.mapperApl = mapperApl;
    }

    @Override
    public UsuarioResponse createUser(CreateUserRequest newUser) {
        if (gateway.findByEmail(newUser.email()) != null) {
            throw new UsuarioJaExisteException(newUser.email());
        }
        Usuarios user = mapperApl.requestToDomain(newUser);
        user.setSenha(encoder.encode(newUser.senha()));
        user.setTipoUsuario("Comprador");

        Usuarios salvo = gateway.salvar(user);
        log.info("Usuário criado com sucesso: id={}, email={}", salvo.getId(), salvo.getEmail());
        return mapperApl.toResponse(salvo);
    }
}
