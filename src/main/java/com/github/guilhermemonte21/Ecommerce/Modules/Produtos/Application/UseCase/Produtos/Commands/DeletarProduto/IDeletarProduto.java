package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.DeletarProduto;

import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.CacheNames;
import org.springframework.cache.annotation.CacheEvict;
import java.util.UUID;

public interface IDeletarProduto {
    @CacheEvict(value = {CacheNames.PRODUTOS, CacheNames.PRODUTO}, allEntries = true)
    void deletar(UUID id);
}
