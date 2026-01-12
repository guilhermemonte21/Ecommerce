package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.BuscarTodosOsProdutos;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarTodosOsProdutos implements IBuscarTodosOsProdutos{
    private final ProdutoGateway gateway;
    private final ProdutoMapperApl mapperApl;

    public BuscarTodosOsProdutos(ProdutoGateway gateway, ProdutoMapperApl mapperApl) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
    }

    @Override
    public List<ProdutoResponse> findAll() {
        List<Produtos> finded =  gateway.findAll();
        return finded.stream().map(mapperApl::ToResponse).toList();
    }
}
