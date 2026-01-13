package com.github.guilhermemonte21.Ecommerce.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapperApl {

    public PedidoResponse toResponse(Pedidos pedido){
        PedidoResponse newPedido = new PedidoResponse(
                pedido.getId(),
                pedido.getComprador().getNome(),
                pedido.getItens().stream().map(c -> c.getId()).toList(),
                pedido.getPreco(),
                pedido.getCriadoEm());
        return newPedido;
    }
}
