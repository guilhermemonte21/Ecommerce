package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.ChangePedidoStatus;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class ChangePedidoStatus implements IChangePedidoStatus {
    private final PedidoGateway gateway;
    private final UsuarioAutenticadoGateway authGateway;

    public ChangePedidoStatus(PedidoGateway gateway, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.authGateway = authGateway;
    }

    @Override
    @Transactional
    public void ChangePedidosStatus(UUID idPedido) {
        Pedidos pedidos = gateway.getById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(idPedido));

        UsuarioAutenticado user = authGateway.get();
        if (!user.getUser().getId().equals(pedidos.getCompradorId())) {
            throw new AcessoNegadoException();
        }

        pedidos.syncStatus();
        gateway.save(pedidos);
    }
}
