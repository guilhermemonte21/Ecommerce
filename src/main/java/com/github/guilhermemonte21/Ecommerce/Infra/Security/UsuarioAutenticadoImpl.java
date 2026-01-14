package com.github.guilhermemonte21.Ecommerce.Infra.Security;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.Login.ILogin;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Infra.Config.UsuarioDetails;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.UsuarioMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAutenticadoImpl implements UsuarioAutenticadoGateway {
    private final UsuarioMapper mapper;

    private final ILogin login;

    public UsuarioAutenticadoImpl(UsuarioMapper mapper, ILogin login) {
        this.mapper = mapper;
        this.login = login;
    }

    @Override
    public UsuarioAutenticado get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                !(auth.getPrincipal() instanceof UsuarioDetails)) {
            return null;
        }

        UsuarioDetails details = (UsuarioDetails) auth.getPrincipal();
        Usuarios user = mapper.toDomain(details.getUser());

        return new UsuarioAutenticado(user);
    }
    }
