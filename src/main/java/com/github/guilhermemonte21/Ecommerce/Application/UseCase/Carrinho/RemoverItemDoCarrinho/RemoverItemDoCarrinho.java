package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.RemoverItemDoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RemoverItemDoCarrinho implements IRemoverItemDoCarrinho{
    private final CarrinhoGateway gateway;

    public RemoverItemDoCarrinho(CarrinhoGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void RemoverItem(UUID IdCarrinho, UUID idProduto){
        Carrinho carrinho = gateway.getById(IdCarrinho).orElseThrow(() -> new RuntimeException("Carrinho não Encontrado"));
        carrinho.atualizarValorTotal();
        carrinho.AtualizadoAgora();
        gateway.DeleteItem(carrinho,idProduto);
    }
}
