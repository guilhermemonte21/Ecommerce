package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.BuscarTodosOsProdutos;

import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.CacheNames;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBuscarTodosOsProdutos {
    @Cacheable(value = CacheNames.PRODUTOS, key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort", sync = true)
    Page<ProdutoResponse> findAll(Pageable pageable);
}
