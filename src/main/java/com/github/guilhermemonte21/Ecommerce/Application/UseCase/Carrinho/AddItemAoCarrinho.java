package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddItemAoCarrinho {
    private final CarrinhoGateway gateway;

    public AddItemAoCarrinho(CarrinhoGateway gateway) {
        this.gateway = gateway;
    }

    public Carrinho AdicionarAoCarrinho(UUID id, UUID IdProduto){
        Carrinho carrinhoComItem =  gateway.add(id, IdProduto);
        carrinhoComItem.atualizarValorTotal();
        return carrinhoComItem;
    }
}
