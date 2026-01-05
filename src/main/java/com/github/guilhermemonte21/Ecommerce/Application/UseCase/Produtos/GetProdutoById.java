package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetProdutoById {
    private final ProdutoGateway gateway;

    public GetProdutoById(ProdutoGateway gateway) {
        this.gateway = gateway;
    }

    public Optional<Produtos> GetProdutoById(UUID id){
        Optional<Produtos> produtos = gateway.GetById(id);
        return produtos;
    }
}
