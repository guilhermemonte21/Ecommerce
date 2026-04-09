package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProdutoMapperApl {

    private final UsuarioGateway gateway;

    public ProdutoMapperApl(UsuarioGateway gateway) {
        this.gateway = gateway;
    }

    public Produtos toDomain(CreateProdutoRequest produtoRequest, UUID idVendedor) {
        Produtos newProduto = new Produtos();
        newProduto.setNomeProduto(produtoRequest.nomeProduto());
        newProduto.setPreco(produtoRequest.preco());
        newProduto.setEstoque(produtoRequest.estoque());
        newProduto.setDescricao(produtoRequest.descricao());
        newProduto.setVendedor(gateway.getById(idVendedor)
                .orElseThrow(() -> new UsuarioNotFoundException(idVendedor)));
        return newProduto;
    }

    public ProdutoResponse toResponse(Produtos produtos) {
        return new ProdutoResponse(
                produtos.getId(),
                produtos.getNomeProduto(),
                produtos.getVendedor() != null ? produtos.getVendedor().getNome() : null,
                produtos.getPreco(),
                produtos.getDescricao(),
                produtos.getEstoque()
        );
    }
}
