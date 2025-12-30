package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl.UsuarioRepositoryImpl;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.ProdutosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.JpaRepository.JpaProdutosRepository;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.JpaRepository.JpaUsuarioRepository;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {
    private JpaProdutosRepository JpaProdutoRepository;
    private final UsuarioRepositoryImpl usuarioRepository;
    private final JpaUsuarioRepository jpaUsuarioRepository;

    public ProdutoMapper(UsuarioRepositoryImpl usuarioRepository, JpaUsuarioRepository jpaUsuarioRepository, JpaProdutosRepository jpaProdutoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.jpaUsuarioRepository = jpaUsuarioRepository;
        JpaProdutoRepository = jpaProdutoRepository;
    }

    public Produtos toDomain(ProdutosEntity entity){
        Produtos produtos = new Produtos();
        produtos.setId(entity.getId());
        produtos.setNomeProduto(entity.getNomeProduto());
        produtos.setPreco(entity.getPreco());
        produtos.setEstoque(entity.getEstoque());
        produtos.setVendedor(usuarioRepository.getById(entity.getVendedor().getId()));
        produtos.setDescricao(entity.getDescricao());
        return produtos;
    }

    public ProdutosEntity toEntity(Produtos entity){
        ProdutosEntity produtos = new ProdutosEntity();
        produtos.setNomeProduto(entity.getNomeProduto());
        produtos.setPreco(entity.getPreco());
        produtos.setEstoque(entity.getEstoque());
        produtos.setVendedor(jpaUsuarioRepository.findById(entity.getVendedor().getId()).orElseThrow(() -> new RuntimeException("Vendedor não encontrado")));
        produtos.setDescricao(entity.getDescricao());
        return produtos;
    }
}
