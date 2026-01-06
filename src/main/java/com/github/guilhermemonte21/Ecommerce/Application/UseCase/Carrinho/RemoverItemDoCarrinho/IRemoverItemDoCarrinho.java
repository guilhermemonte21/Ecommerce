package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.RemoverItemDoCarrinho;

import java.util.UUID;

public interface IRemoverItemDoCarrinho {
    void RemoverItem(UUID IdCarrinho, UUID idProduto);
}
