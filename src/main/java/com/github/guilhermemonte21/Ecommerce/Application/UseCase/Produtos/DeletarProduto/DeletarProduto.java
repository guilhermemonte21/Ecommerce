package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.DeletarProduto;

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
        Optional<Produtos> produtoById = gateway.GetById(id);
        if (produtoById.isEmpty()){
            throw new RuntimeException("Produto Não encontrado");
        }
        if (!IdUser.equals(produtoById.get().getVendedor().getId())){
            throw new RuntimeException("Você não é dono do produto");
        }
        gateway.Delete(produtoById.get());
    }
}
