package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.GetProdutoById;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;

import java.util.UUID;

public class GetProdutoById implements IGetProdutoById {

    private final ProdutoGateway gateway;
    private final ProdutoMapperApl mapperApl;

    public GetProdutoById(ProdutoGateway gateway, ProdutoMapperApl mapperApl) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
    }

    @Override
    public ProdutoResponse getProdutoById(UUID id) {
        Produtos produtos = gateway.getById(id).orElseThrow(() -> new ProdutoNotFoundException(id));
        return mapperApl.toResponse(produtos);
    }
}
