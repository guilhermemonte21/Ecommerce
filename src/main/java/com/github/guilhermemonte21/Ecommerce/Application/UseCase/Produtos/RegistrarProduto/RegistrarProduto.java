package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.RegistrarProduto;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.stereotype.Service;

@Service
public class RegistrarProduto implements IRegistrarProduto{

    private final ProdutoGateway gateway;
    private final ProdutoMapperApl produtoMapper;

    public RegistrarProduto(ProdutoGateway gateway, ProdutoMapperApl produtoMapper) {
        this.gateway = gateway;
        this.produtoMapper = produtoMapper;
    }

    @Override
    public Produtos Create(CreateProdutoRequest produtos){
        Produtos newProd = produtoMapper.toDomain(produtos);

        return gateway.salvar(newProd);
    }
}
