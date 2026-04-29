package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.RegistrarProduto;

import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.CacheNames;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;

import org.springframework.cache.annotation.CacheEvict;

public interface IRegistrarProduto {
    @CacheEvict(value = CacheNames.PRODUTOS, allEntries = true)
    ProdutoResponse create(CreateProdutoRequest produtos);
}
