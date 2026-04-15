package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import org.springframework.stereotype.Component;

@Component
public class CarrinhoMapperApl {

    public CarrinhoMapperApl() {
    }

    public CarrinhoResponse domainToResponse(Carrinho carrinho) {
        return new CarrinhoResponse(
                carrinho.getId(),
                carrinho.getItens(),
                carrinho.getCompradorId(),
                carrinho.getValorTotal(),
                carrinho.getAtualizadoEm());
    }
}
