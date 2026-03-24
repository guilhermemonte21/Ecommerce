package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarEstoque;

import java.util.UUID;

public interface IAtualizarEstoque {
    Long atualizarEstoque(UUID idProduto, Long quantity);
}
