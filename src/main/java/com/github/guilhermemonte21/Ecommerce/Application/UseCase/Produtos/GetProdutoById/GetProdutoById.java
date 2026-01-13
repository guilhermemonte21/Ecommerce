package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.GetProdutoById;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetProdutoById implements IGetProdutoById{
    private final ProdutoGateway gateway;
    private final ProdutoMapperApl mapperApl;

    public GetProdutoById(ProdutoGateway gateway, ProdutoMapperApl mapperApl) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
    }

    @Override
    public ProdutoResponse GetProdutoById(UUID id){
        Produtos produtos = gateway.GetById(id).orElseThrow(() -> new ProdutoNotFoundException(id));
        return mapperApl.ToResponse(produtos);


    }
}
