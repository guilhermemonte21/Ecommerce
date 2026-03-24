package com.github.guilhermemonte21.Ecommerce.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProdutoGateway {
    Produtos salvar(Produtos produtos);

    Optional<Produtos> getById(UUID id);

    Optional<Produtos> getByIdComLock(UUID id);

    void delete(Produtos produtos);

    Page<Produtos> findAll(Pageable pageable);
}
