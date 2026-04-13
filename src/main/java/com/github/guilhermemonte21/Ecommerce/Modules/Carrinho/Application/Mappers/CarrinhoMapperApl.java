package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho.CreateCarrinhoDTO.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CarrinhoMapperApl {

    public CarrinhoMapperApl() {
    }

    public Carrinho createRequestToDomain(CreateCarrinhoRequest carrinhoRequest, UUID idComprador) {
        Carrinho carrinho = new Carrinho();
        carrinho.setCompradorId(idComprador);
        carrinho.setProdutoIds(carrinhoRequest.getProdutosIds());
        return carrinho;
    }

    public CarrinhoResponse domainToResponse(Carrinho carrinho) {
        return new CarrinhoResponse(
                carrinho.getId(),
                carrinho.getProdutoIds(),
                carrinho.getCompradorId(),
                carrinho.getValorTotal(),
                carrinho.getAtualizadoEm());
    }
}
