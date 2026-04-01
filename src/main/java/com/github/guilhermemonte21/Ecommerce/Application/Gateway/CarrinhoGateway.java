package com.github.guilhermemonte21.Ecommerce.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;

import java.util.Optional;
import java.util.UUID;

public interface CarrinhoGateway {
    Carrinho save(Carrinho carrinhoEntity);

    Optional<Carrinho> getById(UUID id);

    void deleteItem(Carrinho carrinho, UUID id);

    Carrinho getByDono(UUID id);
}
