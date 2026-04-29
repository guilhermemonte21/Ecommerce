package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Queries.BuscarTodosOsProdutos;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.IProdutoQueryGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class BuscarTodosOsProdutos implements IBuscarTodosOsProdutos {

    private final IProdutoQueryGateway gateway;

    public BuscarTodosOsProdutos(IProdutoQueryGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Page<ProdutoResponse> findAll(Pageable pageable) {
        return gateway.findAll(pageable);
    }
}
