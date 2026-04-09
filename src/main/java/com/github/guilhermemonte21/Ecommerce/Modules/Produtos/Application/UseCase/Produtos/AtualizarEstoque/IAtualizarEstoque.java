package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.AtualizarEstoque;

import java.util.UUID;

public interface IAtualizarEstoque {
    Long atualizarEstoque(UUID idProduto, Long quantity);
}
