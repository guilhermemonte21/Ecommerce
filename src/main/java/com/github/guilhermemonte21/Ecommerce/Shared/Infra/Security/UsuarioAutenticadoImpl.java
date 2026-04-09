package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Security;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.UsuarioDetails;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Infra.Mappers.UsuarioMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAutenticadoImpl implements UsuarioAutenticadoGateway {
    private final UsuarioMapper mapper;

    public UsuarioAutenticadoImpl(UsuarioMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UsuarioAutenticado get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                !(auth.getPrincipal() instanceof UsuarioDetails)) {
            throw new AcessoNegadoException();
        }

        UsuarioDetails details = (UsuarioDetails) auth.getPrincipal();
        Usuarios user = mapper.toDomain(details.getUser());

        return new UsuarioAutenticado(user);
    }
}
