package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoDoVendedorResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoDoVendedorMapperApl {

    public PedidoDoVendedorResponse toResponse(PedidoDoVendedor domain) {
        List<String> produtos = domain.getProdutos().stream()
                .map(Produtos::getNomeProduto)
                .toList();

        return new PedidoDoVendedorResponse(
                domain.getId(),
                domain.getVendedor().getNome(),
                produtos,
                domain.getValor(),
                domain.getStatus() != null ? domain.getStatus().name() : null
        );
    }
}
