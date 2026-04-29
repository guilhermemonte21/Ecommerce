package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.AtualizarProduto;

import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.CacheNames;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;

import org.springframework.cache.annotation.CacheEvict;

import java.util.UUID;

public interface IAtualizarProduto {
    @CacheEvict(value = {CacheNames.PRODUTOS, CacheNames.PRODUTO}, allEntries = true)
    ProdutoResponse atualizar(UUID idProduto, CreateProdutoRequest produtos);
}
