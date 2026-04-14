package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetPedidosByComprador;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Mappers.PedidoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;

import java.util.List;

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
    public List<PedidoResponse> getPedidosByComprador() {
        UsuarioAutenticado user = authGateway.get();
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }

        List<Pedidos> pedidos = gateway.getPedidosByComprador(user.getUser().getId());

        return pedidos.stream().map(mapperApl::toResponse).toList();
    }
}
