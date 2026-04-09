package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.BuscarTodosOsProdutos;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers.ProdutoMapperApl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class BuscarTodosOsProdutos implements IBuscarTodosOsProdutos {

    private final ProdutoGateway gateway;
    private final ProdutoMapperApl mapperApl;

    public BuscarTodosOsProdutos(ProdutoGateway gateway, ProdutoMapperApl mapperApl) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
    }

    @Override
    public Page<ProdutoResponse> findAll(Pageable pageable) {
        return gateway.findAll(pageable).map(mapperApl::toResponse);
    }
}
