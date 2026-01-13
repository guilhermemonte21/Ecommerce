package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.RemoverItemDoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoverItemDoCarrinho implements IRemoverItemDoCarrinho{
    private final CarrinhoGateway gateway;

    public RemoverItemDoCarrinho(CarrinhoGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    @Transactional
    public void RemoverItem(UUID IdCarrinho, UUID idProduto){
        Carrinho carrinho = gateway.getById(IdCarrinho).orElseThrow(() -> new CarrinhoNotFoundException(IdCarrinho));
        gateway.DeleteItem(carrinho,idProduto);
        carrinho.atualizarValorTotal();
        carrinho.AtualizadoAgora();

    }
}
