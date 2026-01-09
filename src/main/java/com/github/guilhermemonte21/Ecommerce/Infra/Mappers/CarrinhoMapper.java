package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl.UsuarioRepositoryImpl;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.CarrinhoEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.ProdutosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaProdutosRepository;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaUsuarioRepository;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarrinhoMapper {
    private final UsuarioRepositoryImpl usuarioRepository;
    private final ProdutoMapper produtoMapper;
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final UsuarioMapper userMapper;
    private final JpaProdutosRepository produtosRepository;

    public CarrinhoMapper(UsuarioRepositoryImpl usuarioRepository, ProdutoMapper produtoMapper, JpaUsuarioRepository jpaUsuarioRepository, UsuarioMapper userMapper, JpaProdutosRepository produtosRepository) {
        this.usuarioRepository = usuarioRepository;
        this.produtoMapper = produtoMapper;
        this.jpaUsuarioRepository = jpaUsuarioRepository;
        this.userMapper = userMapper;
        this.produtosRepository = produtosRepository;
    }

    public Carrinho toDomain(CarrinhoEntity entity){
        Carrinho carrinho = new Carrinho();

        carrinho.setId(entity.getId());
        carrinho.setComprador(jpaUsuarioRepository.findById(entity.getComprador().getId())
                .map(userMapper::toDomain).orElseThrow(() -> new RuntimeException("Comprador não encontrado"))
        );
        carrinho.setItens(entity.getItens().stream().map(c ->
                produtoMapper.toDomain(c)).collect(Collectors.toList()));
        carrinho.setValorTotal(entity.getValorTotal());
        carrinho.setAtualizadoEm(OffsetDateTime.now());
        return carrinho;
    }

    public CarrinhoEntity toEntity(Carrinho entity){
        CarrinhoEntity carrinho = new CarrinhoEntity();

        carrinho.setId(entity.getId());
        UsuariosEntity user = jpaUsuarioRepository
                .findById(entity.getComprador().getId())
                .orElseThrow(() -> new RuntimeException("Comprador não Encontrado"));

        carrinho.setComprador(user);

        List<ProdutosEntity> produtos = entity.getItens().stream()
                .map(p -> produtosRepository.findById(p.getId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado")))
                .collect(Collectors.toList());

        carrinho.setItens(produtos);
        carrinho.setValorTotal(entity.getValorTotal());
        carrinho.setAtualizadoEm(OffsetDateTime.now());
        return carrinho;
    }
}
