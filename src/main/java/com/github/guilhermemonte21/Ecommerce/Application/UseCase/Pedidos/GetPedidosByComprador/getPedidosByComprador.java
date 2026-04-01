package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidosByComprador;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.PedidoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;

import java.util.List;
import java.util.UUID;

public class GetPedidosByComprador implements IGetPedidosByComprador {

    private final PedidoGateway gateway;
    private final UsuarioAutenticadoGateway authGateway;
    private final PedidoMapperApl mapperApl;

    public GetPedidosByComprador(PedidoGateway gateway, UsuarioAutenticadoGateway authGateway,
            PedidoMapperApl mapperApl) {
        this.gateway = gateway;
        this.authGateway = authGateway;
        this.mapperApl = mapperApl;
    }

    @Override
    public List<PedidoResponse> getPedidosByComprador(UUID idComprador) {

        UsuarioAutenticado user = authGateway.get();
        if (!user.getUser().getId().equals(idComprador)) {
            throw new AcessoNegadoException();
        }
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }

        List<Pedidos> pedidos = gateway.getPedidosByComprador(idComprador);

        return pedidos.stream().map(mapperApl::toResponse).toList();
    }
}
