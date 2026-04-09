package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.RemoverItemDoCarrinho;

import java.util.UUID;

public interface IRemoverItemDoCarrinho {
    void removerItem(UUID idCarrinho, UUID idProduto);
}
