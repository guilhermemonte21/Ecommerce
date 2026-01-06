package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.LimparCarrinho;

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
    public void LimparCarrinho(UUID IdUser, UUID IdCarrinho) {
        Carrinho carrinho = gateway.getById(IdCarrinho)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));
        if (!IdUser.equals(carrinho.getComprador().getId())){
            throw new RuntimeException("Você não é o dono do carrinho");
        }
        gateway.LimparCarrinho(carrinho);
    }
}
