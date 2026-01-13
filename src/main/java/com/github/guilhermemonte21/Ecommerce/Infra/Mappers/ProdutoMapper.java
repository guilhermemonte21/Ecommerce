package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl.UsuarioRepositoryImpl;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.ProdutosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaProdutosRepository;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaUsuarioRepository;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {
    private JpaProdutosRepository JpaProdutoRepository;
    private final UsuarioRepositoryImpl usuarioRepository;
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final UsuarioMapper userMapper;

    public ProdutoMapper(JpaProdutosRepository jpaProdutoRepository, UsuarioRepositoryImpl usuarioRepository, JpaUsuarioRepository jpaUsuarioRepository, UsuarioMapper userMapper) {
        JpaProdutoRepository = jpaProdutoRepository;
        this.usuarioRepository = usuarioRepository;
        this.jpaUsuarioRepository = jpaUsuarioRepository;
        this.userMapper = userMapper;
    }

    public Produtos toDomain(ProdutosEntity entity){
        Produtos produtos = new Produtos();
        produtos.setId(entity.getId());
        produtos.setNomeProduto(entity.getNomeProduto());
        produtos.setPreco(entity.getPreco());
        produtos.setEstoque(entity.getEstoque());
        produtos.setVendedor(userMapper.toDomain(entity.getVendedor()));
        produtos.setDescricao(entity.getDescricao());
        return produtos;
    }

    public ProdutosEntity toEntity(Produtos domain) {

        ProdutosEntity entity = new ProdutosEntity();
        entity.setId(domain.getId());
        entity.setNomeProduto(domain.getNomeProduto());
        entity.setPreco(domain.getPreco());
        entity.setEstoque(domain.getEstoque());
        entity.setDescricao(domain.getDescricao());

        UsuariosEntity vendedor = jpaUsuarioRepository
                .findById(domain.getVendedor().getId())
                .orElseThrow(() -> new UsuarioNotFoundException(domain.getVendedor().getId()));

        entity.setVendedor(vendedor);
        return entity;
    }
}
