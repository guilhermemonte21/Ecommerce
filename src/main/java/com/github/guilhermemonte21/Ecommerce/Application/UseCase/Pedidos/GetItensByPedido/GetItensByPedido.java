package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetItensByPedido;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetItensByPedido implements IGetItensByPedido{
    private final PedidoGateway pedidoGateway;

    public GetItensByPedido(PedidoGateway pedidoGateway) {
        this.pedidoGateway = pedidoGateway;
    }

    @Override
    public List<PedidoDoVendedor> get(UUID IdPedido) {
        Pedidos pedido = pedidoGateway.getById(IdPedido).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        return pedido.getItens();
    }
}
