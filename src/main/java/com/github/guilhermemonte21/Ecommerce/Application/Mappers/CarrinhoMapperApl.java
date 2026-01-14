package com.github.guilhermemonte21.Ecommerce.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CreateCarrinhoDTO.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CarrinhoMapperApl {
    private final ProdutoGateway prodGateway;

    private final UsuarioGateway usuGateway;

    public CarrinhoMapperApl(ProdutoGateway prodGateway, UsuarioGateway usuGateway) {
        this.prodGateway = prodGateway;
        this.usuGateway = usuGateway;
    }

    public Carrinho CreateResquesttoDomain(CreateCarrinhoRequest carrinhoRequest, UUID IdComprador){
        Carrinho carrinho = new Carrinho();
        carrinho.setComprador(usuGateway.getById(IdComprador).orElseThrow(() -> new UsuarioNotFoundException(IdComprador)));
        List<Produtos> prods = carrinhoRequest.getProdutosIds()
                .stream()
                .map(c -> prodGateway.GetById(c)
                        .orElseThrow(() -> new ProdutoNotFoundException(c)))
                .collect(Collectors.toList());


        carrinho.setItens(prods);
       return carrinho;
    }
    public CarrinhoResponse DomainToResponse(Carrinho carrinho){
        CarrinhoResponse Dto = new CarrinhoResponse(
                carrinho.getId(),
                carrinho.getItens().stream().map(c-> c.getNomeProduto()).toList(),
                carrinho.getComprador().getNome(),
                carrinho.getValorTotal(),
                carrinho.getAtualizadoEm()
        );
        return Dto;
    }
}
