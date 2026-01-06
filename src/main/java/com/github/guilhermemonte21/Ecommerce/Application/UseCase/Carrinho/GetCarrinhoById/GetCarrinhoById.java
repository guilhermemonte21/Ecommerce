package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.GetCarrinhoById;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetCarrinhoById implements IGetCarrinhoById{
    private CarrinhoGateway gateway;

    public GetCarrinhoById(CarrinhoGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Optional<Carrinho> FindCarrinhoById(UUID Id){
        return gateway.getById(Id);

    }
}
