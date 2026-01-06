package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarProduto;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AtualizarProduto implements IAtualizarProduto{
    private final ProdutoGateway gateway;

    public AtualizarProduto(ProdutoGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Produtos Atualizar(UUID IdUser, UUID IdProduto, Produtos produtos){

       Produtos ProdById = gateway.GetById(IdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!ProdById.getVendedor().getId().equals(IdUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Usuário não é o vendedor do produto");
        }
       ProdById.setVendedor(produtos.getVendedor());
       ProdById.setEstoque(produtos.getEstoque());
       ProdById.setDescricao(produtos.getDescricao());
       ProdById.setNomeProduto(produtos.getNomeProduto());
       ProdById.setPreco(produtos.getPreco());
       return gateway.salvar(ProdById);
    }
}
