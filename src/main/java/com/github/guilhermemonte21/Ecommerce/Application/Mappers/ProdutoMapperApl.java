package com.github.guilhermemonte21.Ecommerce.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProdutoMapperApl {
    private final UsuarioGateway gateway;

    public ProdutoMapperApl(UsuarioGateway gateway) {
        this.gateway = gateway;
    }

    public Produtos toDomain(CreateProdutoRequest produtoRequest, UUID IdVendedor){
        Produtos newProduto = new Produtos();
        newProduto.setNomeProduto(produtoRequest.nomeProduto());
        newProduto.setPreco(produtoRequest.preco());
        newProduto.setEstoque(produtoRequest.estoque());
        newProduto.setDescricao(produtoRequest.descricao());
        newProduto.setVendedor(gateway.getById(IdVendedor).orElseThrow(() -> new UsuarioNotFoundException(IdVendedor)));
        return newProduto;
    }
    public ProdutoResponse ToResponse(Produtos produtos){
        ProdutoResponse response = new ProdutoResponse(
                produtos.getId(),
                produtos.getNomeProduto(),
                produtos.getVendedor().getNome(),
                produtos.getPreco(),
                produtos.getDescricao(),
                produtos.getEstoque()
        );
        return response;
    }
}
