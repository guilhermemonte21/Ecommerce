package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Service;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioInativoException;
import java.util.UUID;

public class CarrinhoAuthorizationService {

    private final UsuarioAutenticadoGateway authGateway;

    public CarrinhoAuthorizationService(UsuarioAutenticadoGateway authGateway) {
        this.authGateway = authGateway;
    }

    public UsuarioAutenticado validarDono(UUID compradorId) {
        UsuarioAutenticado user = authGateway.get();
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
        if (!user.getUser().getId().equals(compradorId)) {
            throw new AcessoNegadoException();
        }
        return user;
    }

    public UsuarioAutenticado validarAtivo() {
        UsuarioAutenticado user = authGateway.get();
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
        return user;
    }
}
