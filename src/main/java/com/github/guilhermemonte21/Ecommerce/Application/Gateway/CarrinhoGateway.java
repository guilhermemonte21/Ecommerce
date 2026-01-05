package com.github.guilhermemonte21.Ecommerce.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarrinhoGateway {
    Carrinho save(Carrinho carrinhoEntity);

    Optional<Carrinho> getById(UUID Id);

    Carrinho add(UUID Id, UUID IdProdutos);

    void DeleteItem(UUID Id, UUID id);
}
