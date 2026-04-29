package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Queries.GetProdutoById;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.IProdutoQueryGateway;

import java.util.UUID;

public class GetProdutoById implements IGetProdutoById {

    private final IProdutoQueryGateway gateway;

    public GetProdutoById(IProdutoQueryGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public ProdutoResponse getProdutoById(UUID id) {
        return gateway.getById(id);
    }
}
