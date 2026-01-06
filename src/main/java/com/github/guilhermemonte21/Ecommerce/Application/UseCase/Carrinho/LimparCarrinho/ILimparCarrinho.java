package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.LimparCarrinho;

import java.util.UUID;

public interface ILimparCarrinho {
    void LimparCarrinho(UUID IdUser, UUID IdProduto);
}
