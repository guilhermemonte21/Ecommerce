package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.AddItemAoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddItemAoCarrinho implements IAddItemAoCarrinho{
    private final CarrinhoGateway gateway;

    public AddItemAoCarrinho(CarrinhoGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Carrinho AdicionarAoCarrinho(UUID id, UUID IdProduto, Long quantidade){
        Carrinho carrinhoComItem =  gateway.add(id, IdProduto, quantidade);
        carrinhoComItem.atualizarValorTotal();
        carrinhoComItem.AtualizadoAgora();
        return carrinhoComItem;
    }
}
