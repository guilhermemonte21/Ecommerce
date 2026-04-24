package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdutoGateway {
    Produtos salvar(Produtos produtos);

    Optional<Produtos> getById(UUID id);

    List<Produtos> findAllByIds(List<UUID> ids);

    Optional<Produtos> getByIdComLock(UUID id);

    void delete(Produtos produtos);

    Page<Produtos> findAll(Pageable pageable);
}
