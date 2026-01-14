package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidoById;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.PedidoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetPedidoById implements IGetPedidoById{
    private final PedidoGateway gateway;
    private final PedidoMapperApl mapperApl;
    private final UsuarioAutenticadoGateway AuthGateway;

    public GetPedidoById(PedidoGateway gateway, PedidoMapperApl mapperApl, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
        AuthGateway = authGateway;
    }

    @Override
    public PedidoResponse pedidoById(UUID IdPedido) {
        Pedidos pedidos = gateway.getById(IdPedido).orElseThrow(() -> new PedidoNotFoundException(IdPedido));
        UsuarioAutenticado user = AuthGateway.get();
        if(!user.getUser().getId().equals(pedidos.getComprador().getId())){
            throw new AcessoNegadoException();
        }
        PedidoResponse response = mapperApl.toResponse(pedidos);

        return response;
    }
}
