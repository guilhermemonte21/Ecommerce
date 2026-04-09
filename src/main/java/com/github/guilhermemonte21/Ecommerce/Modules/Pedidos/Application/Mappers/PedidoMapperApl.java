package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapperApl {

    public PedidoResponse toResponse(Pedidos pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getComprador().getNome(),
                pedido.getItens().stream().map(c -> c.getId()).toList(),
                pedido.getEndereco(),
                pedido.getPreco(),
                pedido.getStatus() != null ? pedido.getStatus().name() : null,
                pedido.getCriadoEm());
    }
}
