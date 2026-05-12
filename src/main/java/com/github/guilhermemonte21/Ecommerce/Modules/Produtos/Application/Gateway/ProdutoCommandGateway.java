package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;

import java.util.Optional;
import java.util.UUID;

public interface ProdutoCommandGateway {
    Produtos salvar(Produtos produto);
    void delete(Produtos produto);
    Optional<Produtos> getById(UUID id);
    Optional<Produtos> getByIdComLock(UUID id);
}
