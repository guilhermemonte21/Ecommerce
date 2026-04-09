package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Infra.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.CreateSellerAcount.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.DesativarConta.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.GetUserById.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Mappers.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.CreateUser.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.Login.*;

@Configuration
public class UsuarioModuleConfig {


    @Bean
    public ILogin login(UsuarioGateway gateway, PasswordEncoder encoder) {
        return new Login(gateway, encoder);
    }

    @Bean
    public IGetUserById getUserById(UsuarioGateway gateway, UsuarioMapperApl mapper) {
        return new GetUserById(gateway, mapper);
    }

    @Bean
    public IMudarAtividadeDaConta mudarAtividadeDaConta(UsuarioGateway gateway, UsuarioAutenticadoGateway authGateway) {
        return new MudarAtividadeDaConta(gateway, authGateway);
    }

    @Bean
    public ICreateUser createUser(UsuarioGateway gateway, PasswordEncoder encoder, UsuarioMapperApl mapper) {
        return new CreateUser(gateway, encoder, mapper);
    }

    @Bean
    public ICreateSellerAcount createSellerAcount(UsuarioGateway gateway, UsuarioMapperApl mapper, UsuarioAutenticadoGateway authGateway) {
        return new CreateSellerAcount(gateway, mapper, authGateway);
    }
}
