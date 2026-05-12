package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Document.ProductDocument;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProdutoMapperApl {

    public Produtos toDomain(CreateProdutoRequest produtoRequest, UUID idVendedor) {
        Produtos newProduto = new Produtos();
        newProduto.setNomeProduto(produtoRequest.nomeProduto());
        newProduto.setPreco(produtoRequest.preco());
        newProduto.setEstoque(produtoRequest.estoque());
        newProduto.setDescricao(produtoRequest.descricao());
        newProduto.setVendedorId(idVendedor);
        return newProduto;
    }

    public ProdutoResponse toResponse(Produtos produtos, String nomeVendedor) {
        return new ProdutoResponse(
                produtos.getId(),
                produtos.getNomeProduto(),
                nomeVendedor,
                produtos.getPreco(),
                produtos.getDescricao(),
                produtos.getEstoque());
    }

    public ProdutoResponse toResponse(ProductDocument doc) {
        return new ProdutoResponse(
                doc.getId(),
                doc.getNomeProduto(),
                doc.getVendedorNome(),
                doc.getPreco(),
                doc.getDescricao(),
                doc.getEstoque());
    }

    public ProductDocument toDocument(Produtos domain, String nomeVendedor) {
        return ProductDocument.builder()
                .id(domain.getId())
                .nomeProduto(domain.getNomeProduto())
                .vendedorId(domain.getVendedorId())
                .vendedorNome(nomeVendedor)
                .descricao(domain.getDescricao())
                .preco(domain.getPreco())
                .estoque(domain.getEstoque())
                .createdAt(java.time.Instant.now())
                .build();
    }
}
