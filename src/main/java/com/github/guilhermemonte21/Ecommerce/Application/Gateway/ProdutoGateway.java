package com.github.guilhermemonte21.Ecommerce.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


public interface ProdutoGateway {
    Produtos salvar(Produtos produtosEntity);

    Optional<Produtos> GetById(UUID Id);

    void Delete(Produtos produtosEntity);

}
