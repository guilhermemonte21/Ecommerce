package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.ProdutosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.JpaRepository.JpaProdutosRepository;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.JpaRepository.JpaUsuarioRepository;

import java.util.Optional;
import java.util.UUID;

public class ProdutoRepositoryImpl implements ProdutoGateway {

    private final JpaProdutosRepository JpaProdutosRepo;
    private final JpaUsuarioRepository JpaUsuarioRepo;

    public ProdutoRepositoryImpl(JpaProdutosRepository jpaProdutosRepo, JpaUsuarioRepository jpaUsuarioRepo) {
        JpaProdutosRepo = jpaProdutosRepo;
        JpaUsuarioRepo = jpaUsuarioRepo;
    }

    @Override
    public Produtos salvar(Produtos produtos) {
        UsuariosEntity Vendedor = JpaUsuarioRepo.findById(produtos.getVendedor().getId()).orElseThrow(() -> new RuntimeException("Vendedor não Encontrado"));

        ProdutosEntity produtosEntity = new ProdutosEntity();
        produtosEntity.setNomeProduto(produtos.getNomeProduto());
        produtosEntity.setVendedor(Vendedor);
        produtosEntity.setDescricao(produtos.getDescricao());
        produtosEntity.setPreco(produtos.getPreco());
        produtosEntity.setEstoque(produtos.getEstoque());

        ProdutosEntity salvo = JpaProdutosRepo.save(produtosEntity);

        return new Produtos(salvo.getId(),salvo.getNomeProduto(), produtos.getVendedor(), salvo.getDescricao(), salvo.getPreco(), salvo.getEstoque());
    }

    @Override
    public Produtos GetById(UUID Id) {
        ProdutosEntity entity = JpaProdutosRepo.findById(Id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        UsuariosEntity vendedorEntity = entity.getVendedor();
        Usuarios vendedor = new Usuarios(vendedorEntity.getId(),vendedorEntity.getNome(), vendedorEntity.getEmail(), vendedorEntity.getCpf(), vendedorEntity.getSenha(), vendedorEntity.getAtivo(),vendedorEntity.getRole());

        return new Produtos(entity.getId(), entity.getNomeProduto(), vendedor, entity.getDescricao(), entity.getPreco(), entity.getEstoque());

    }

    @Override
    public void Delete(Produtos produtos) {
        ProdutosEntity produtosEntity = JpaProdutosRepo.findById(produtos.getId()).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        JpaProdutosRepo.delete(produtosEntity);
    }
}



