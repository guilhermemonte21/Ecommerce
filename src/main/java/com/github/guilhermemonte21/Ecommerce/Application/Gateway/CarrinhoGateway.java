package com.github.guilhermemonte21.Ecommerce.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;

import java.util.Optional;
import java.util.UUID;

public interface CarrinhoGateway {
    Carrinho save(Carrinho carrinhoEntity);

    Optional<Carrinho> getById(UUID Id);

    Carrinho add(UUID Id, Produtos produto, Long quantity);

    void DeleteItem(Carrinho carrinho, UUID id);

    void LimparCarrinho(Carrinho carrinho);
}
