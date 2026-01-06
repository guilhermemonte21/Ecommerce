package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarEstoque;

import java.util.UUID;

public interface IAtualizarEstoque {
    public Long AtualizarEstoque(UUID idUser, UUID idProduto, Long quantity);
}
