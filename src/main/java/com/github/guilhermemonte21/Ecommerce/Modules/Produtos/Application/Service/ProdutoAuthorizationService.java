package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Service;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Enum.TipoUsuario;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioInativoException;

import java.util.UUID;

public class ProdutoAuthorizationService {

    private final UsuarioAutenticadoGateway authGateway;

    public ProdutoAuthorizationService(UsuarioAutenticadoGateway authGateway) {
        this.authGateway = authGateway;
    }

    public UsuarioAutenticado validarProprietario(UUID vendedorId) {
        UsuarioAutenticado user = authGateway.get();
        validarAtivo(user);
        if (!user.getUser().getId().equals(vendedorId)) {
            throw new AcessoNegadoException();
        }
        return user;
    }

    public UsuarioAutenticado validarVendedorAtivo() {
        UsuarioAutenticado user = authGateway.get();
        validarAtivo(user);
        if (!TipoUsuario.VENDEDOR.matches(user.getUser().getTipoUsuario())) {
            throw new AcessoNegadoException();
        }
        return user;
    }

    private void validarAtivo(UsuarioAutenticado user) {
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
    }
}
