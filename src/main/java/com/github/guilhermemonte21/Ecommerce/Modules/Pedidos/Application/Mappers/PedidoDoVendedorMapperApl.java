package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoDoVendedorResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.PedidoDoVendedor;
import org.springframework.stereotype.Component;

@Component
public class PedidoDoVendedorMapperApl {

    public PedidoDoVendedorResponse toResponse(PedidoDoVendedor domain) {
        return new PedidoDoVendedorResponse(
                domain.getId(),
                domain.getVendedorId(),
                domain.getProdutoId(),
                domain.getNomeProduto(),
                domain.getPrecoUnitario(),
                domain.getQuantidade(),
                domain.getValor(),
                domain.getStatus() != null ? domain.getStatus().name() : null);
    }
}
