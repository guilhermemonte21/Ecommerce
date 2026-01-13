package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.DeletarProduto;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeletarProduto implements IDeletarProduto{
    private final ProdutoGateway gateway;

    public DeletarProduto(ProdutoGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void Deletar(UUID IdUser, UUID id){
        Produtos produtoById = gateway.GetById(id).orElseThrow(() -> new ProdutoNotFoundException(id));


        gateway.Delete(produtoById);
    }
}
