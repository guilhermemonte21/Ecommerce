package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.ChangePedidoStatus;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Service.PedidoAuthorizationService;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

public class ChangePedidoStatus implements IChangePedidoStatus {
    private final PedidoGateway gateway;
    private final PedidoAuthorizationService authorizationService;

    public ChangePedidoStatus(PedidoGateway gateway, PedidoAuthorizationService authorizationService) {
        this.gateway = gateway;
        this.authorizationService = authorizationService;
    }

    @Override
    @Transactional
    public void mudarStatus(UUID idPedido) {
        Pedidos pedidos = gateway.getById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(idPedido));

        authorizationService.validarComprador(pedidos.getCompradorId());

        pedidos.syncStatus();
        gateway.save(pedidos);
    }
}