package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProdutoBatchGateway {
    Page<Produtos> findAll(Pageable pageable);
    List<Produtos> saveAll(List<Produtos> produtos);
    List<Produtos> findAllByIds(List<UUID> ids);
}
