package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.GetCarrinhoById;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.CarrinhoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GetCarrinhoById implements IGetCarrinhoById{
    private CarrinhoGateway gateway;
    private CarrinhoMapperApl mapper;

    public GetCarrinhoById(CarrinhoGateway gateway, CarrinhoMapperApl mapper) {
        this.gateway = gateway;
        this.mapper = mapper;
    }

    @Override
    public CarrinhoResponse FindCarrinhoById(UUID Id){
        Carrinho carrinho = gateway.getById(Id).orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));
        return mapper.DomainToResponse(carrinho);


    }
}
