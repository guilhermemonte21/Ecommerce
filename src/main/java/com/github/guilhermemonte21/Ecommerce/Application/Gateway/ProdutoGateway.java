package com.github.guilhermemonte21.Ecommerce.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ProdutoGateway {
    Produtos salvar(Produtos produtos);

    Optional<Produtos> GetById(UUID Id);

    void Delete(Produtos produtos);

    List<Produtos> findAll();

}
