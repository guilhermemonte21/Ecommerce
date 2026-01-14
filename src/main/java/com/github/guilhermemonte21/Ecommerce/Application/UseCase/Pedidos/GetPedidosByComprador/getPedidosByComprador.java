package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidosByComprador;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;

import java.util.List;
import java.util.UUID;

public class getPedidosByComprador implements IGetPedidosByComprador{

    private final PedidoGateway gateway;
    private final UsuarioAutenticadoGateway AuthGateway;

    public getPedidosByComprador(PedidoGateway gateway, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        AuthGateway = authGateway;
    }

    @Override
    public List<Pedidos> getPedidosByComprador(UUID IdComprador) {
        List<Pedidos> pedidos = gateway.getPedidosByComprador(IdComprador);
        UsuarioAutenticado user = AuthGateway.get();
        if(!user.getUser().getId().equals(IdComprador)){
            throw new AcessoNegadoException();
        }

        if (pedidos.isEmpty()){
            throw new UsuarioNotFoundException(IdComprador);
        }

        return pedidos;
    }
}
