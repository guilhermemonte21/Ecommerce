package com.github.guilhermemonte21.Ecommerce.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;

import java.util.Optional;
import java.util.UUID;

public interface CarrinhoGateway {
    Carrinho save(Carrinho carrinhoEntity);

    Optional<Carrinho> getById(UUID id);

    Carrinho add(UUID id, Produtos produto, Long quantity);

    void deleteItem(Carrinho carrinho, UUID id);

    void limparCarrinho(Carrinho carrinho);
}
