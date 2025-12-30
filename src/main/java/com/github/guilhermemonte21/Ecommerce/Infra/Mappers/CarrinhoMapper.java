package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl.UsuarioRepositoryImpl;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.CarrinhoEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.JpaRepository.JpaUsuarioRepository;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@Component
public class CarrinhoMapper {
    private final UsuarioRepositoryImpl usuarioRepository;
    private final ProdutoMapper produtoMapper;
    private final JpaUsuarioRepository jpaUsuarioRepository;

    public CarrinhoMapper(UsuarioRepositoryImpl usuarioRepository, ProdutoMapper produtoMapper, JpaUsuarioRepository jpaUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.produtoMapper = produtoMapper;
        this.jpaUsuarioRepository = jpaUsuarioRepository;
    }

    public Carrinho toDomain(CarrinhoEntity entity){
        Carrinho carrinho = new Carrinho();

        carrinho.setId(entity.getId());
        carrinho.setComprador(usuarioRepository.getById(entity.getComprador().getId()));
        carrinho.setItens(entity.getItens().stream().map(c ->
                produtoMapper.toDomain(c)).collect(Collectors.toList()));
        carrinho.setValorTotal(entity.getValorTotal());
        carrinho.setAtualizadoEm(OffsetDateTime.now());
        return carrinho;
    }

    public CarrinhoEntity toEntity(Carrinho entity){
        CarrinhoEntity carrinho = new CarrinhoEntity();

        carrinho.setId(entity.getId());
        carrinho.setComprador(jpaUsuarioRepository.findById(entity.getComprador().getId()).orElseThrow(() -> new RuntimeException("Comprador não Encontrado")));
        carrinho.setItens(entity.getItens().stream().map(c ->
                produtoMapper.toEntity(c)).collect(Collectors.toList()));
        carrinho.setValorTotal(entity.getValorTotal());
        carrinho.setAtualizadoEm(OffsetDateTime.now());
        return carrinho;
    }
}
