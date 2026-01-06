package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.CriarCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.CarrinhoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import org.springframework.stereotype.Service;

@Service
public class CriarCarrinho implements ICriarCarrinho{
    private final CarrinhoGateway gateway;
    private final CarrinhoMapperApl mapper;

    public CriarCarrinho(CarrinhoGateway gateway, CarrinhoMapperApl mapper) {
        this.gateway = gateway;
        this.mapper = mapper;
    }

    public Carrinho Criar(CreateCarrinhoRequest carrinho){
        Carrinho carrinho1 = mapper.toDomain(carrinho);
        carrinho1.atualizarValorTotal();
        carrinho1.AtualizadoAgora();
        return gateway.save(carrinho1);
    }
}
