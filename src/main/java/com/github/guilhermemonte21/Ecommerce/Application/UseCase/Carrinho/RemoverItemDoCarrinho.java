package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RemoverItemDoCarrinho {
    private final CarrinhoGateway gateway;

    public RemoverItemDoCarrinho(CarrinhoGateway gateway) {
        this.gateway = gateway;
    }

    public void RemoverItem(UUID Id, UUID id){
        gateway.DeleteItem(Id,id);
    }
}
