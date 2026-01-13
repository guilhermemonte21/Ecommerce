package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.LimparCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LimparCarrinho implements ILimparCarrinho{
    private final CarrinhoGateway gateway;

    public LimparCarrinho(CarrinhoGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void LimparCarrinho( UUID IdCarrinho) {
        Carrinho carrinho = gateway.getById(IdCarrinho)
                .orElseThrow(() -> new CarrinhoNotFoundException(IdCarrinho));

        gateway.LimparCarrinho(carrinho);
    }
}
