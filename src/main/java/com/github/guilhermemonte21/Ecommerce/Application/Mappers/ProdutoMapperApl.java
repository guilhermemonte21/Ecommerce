package com.github.guilhermemonte21.Ecommerce.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapperApl {
    private final UsuarioGateway gateway;

    public ProdutoMapperApl(UsuarioGateway gateway) {
        this.gateway = gateway;
    }

    public Produtos toDomain(CreateProdutoRequest produtoRequest){
        Produtos newProduto = new Produtos();
        newProduto.setNomeProduto(produtoRequest.nomeProduto());
        newProduto.setPreco(produtoRequest.preco());
        newProduto.setEstoque(produtoRequest.estoque());
        newProduto.setDescricao(produtoRequest.descricao());
        newProduto.setVendedor(gateway.getById(produtoRequest.vendedor()).orElseThrow(() -> new UsuarioNotFoundException(produtoRequest.vendedor())));
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
