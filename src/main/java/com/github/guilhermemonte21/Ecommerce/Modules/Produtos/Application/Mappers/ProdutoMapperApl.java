package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Document.ProductDocument;
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
        gateway.getById(idVendedor).orElseThrow(() -> new UsuarioNotFoundException(idVendedor));
        newProduto.setVendedorId(idVendedor);
        return newProduto;
    }

    public ProdutoResponse toResponse(Produtos produtos) {
        return new ProdutoResponse(
                produtos.getId(),
                produtos.getNomeProduto(),
                produtos.getVendedorId() != null
                        ? gateway.getById(produtos.getVendedorId()).map(u -> u.getNome()).orElse(null)
                        : null,
                produtos.getPreco(),
                produtos.getDescricao(),
                produtos.getEstoque());
    }

    public ProdutoResponse toResponse(ProductDocument doc) {
        return new ProdutoResponse(
                doc.getId(),
                doc.getNomeProduto(),
                doc.getVendedorId() != null
                        ? gateway.getById(doc.getVendedorId()).map(u -> u.getNome()).orElse(null)
                        : null,
                doc.getPreco(),
                doc.getDescricao(),
                doc.getEstoque());
    }

    public ProductDocument toDocument(Produtos domain) {
        return ProductDocument.builder()
                .id(domain.getId())
                .nomeProduto(domain.getNomeProduto())
                .vendedorId(domain.getVendedorId())
                .descricao(domain.getDescricao())
                .preco(domain.getPreco())
                .estoque(domain.getEstoque())
                .version(domain.getVersion())
                .build();
    }
}
