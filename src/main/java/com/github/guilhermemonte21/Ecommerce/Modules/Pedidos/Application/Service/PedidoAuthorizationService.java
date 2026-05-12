package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Service;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.AcessoNegadoException;
import java.util.UUID;

public class PedidoAuthorizationService {

    private final UsuarioAutenticadoGateway authGateway;

    public PedidoAuthorizationService(UsuarioAutenticadoGateway authGateway) {
        this.authGateway = authGateway;
    }


    public UsuarioAutenticado validarComprador(UUID compradorId) {
        UsuarioAutenticado user = authGateway.get();
        if (!compradorId.equals(user.getUser().getId())) {
            throw new AcessoNegadoException();
        }
        return user;
    }
}
