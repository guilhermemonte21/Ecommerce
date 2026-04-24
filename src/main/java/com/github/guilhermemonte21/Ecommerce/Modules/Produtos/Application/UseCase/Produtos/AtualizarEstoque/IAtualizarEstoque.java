package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.AtualizarEstoque;

import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.CacheNames;
import org.springframework.cache.annotation.CacheEvict;
import java.util.UUID;

public interface IAtualizarEstoque {
    @CacheEvict(value = {CacheNames.PRODUTOS, CacheNames.PRODUTO}, allEntries = true)
    Long atualizarEstoque(UUID idProduto, Long quantity);
}
