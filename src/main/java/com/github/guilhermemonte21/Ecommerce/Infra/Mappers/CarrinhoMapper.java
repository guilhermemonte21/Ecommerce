package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.CarrinhoEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.ProdutosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaProdutosRepository;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaUsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarrinhoMapper {
    private final ProdutoMapper produtoMapper;
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final UsuarioMapper userMapper;
    private final JpaProdutosRepository produtosRepository;

    public CarrinhoMapper(ProdutoMapper produtoMapper, JpaUsuarioRepository jpaUsuarioRepository,
            UsuarioMapper userMapper, JpaProdutosRepository produtosRepository) {
        this.produtoMapper = produtoMapper;
        this.jpaUsuarioRepository = jpaUsuarioRepository;
        this.userMapper = userMapper;
        this.produtosRepository = produtosRepository;
    }

    public Carrinho toDomain(CarrinhoEntity entity) {
        Carrinho carrinho = new Carrinho();
        carrinho.setId(entity.getId());
        carrinho.setComprador(userMapper.toDomain(entity.getComprador()));
        carrinho.setItens(entity.getItens().stream()
                .map(produtoMapper::toDomain)
                .collect(Collectors.toList()));
        carrinho.setValorTotal(entity.getValorTotal());
        carrinho.setAtualizadoEm(entity.getAtualizadoEm());
        return carrinho;
    }

    public CarrinhoEntity toEntity(Carrinho domain) {
        CarrinhoEntity entity = new CarrinhoEntity();
        entity.setId(domain.getId());

        // P1 fix: use getReferenceById (proxy, no SELECT) instead of findById
        UsuariosEntity user = jpaUsuarioRepository.getReferenceById(domain.getComprador().getId());
        entity.setComprador(user);

        // P1 fix: use getReferenceById for each product (proxy, no SELECT per item)
        List<ProdutosEntity> produtos = domain.getItens().stream()
                .map(p -> produtosRepository.getReferenceById(p.getId()))
                .collect(Collectors.toList());
        entity.setItens(produtos);

        entity.setValorTotal(domain.getValorTotal());
        entity.setAtualizadoEm(domain.getAtualizadoEm());
        return entity;
    }
}
