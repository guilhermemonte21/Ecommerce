package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.DeletarProduto;

import java.util.UUID;

public interface IDeletarProduto {
    void Deletar(UUID IdUser, UUID id);
}
