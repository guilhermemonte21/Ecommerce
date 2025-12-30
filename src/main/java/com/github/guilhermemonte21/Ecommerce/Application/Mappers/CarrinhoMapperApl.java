package com.github.guilhermemonte21.Ecommerce.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarrinhoMapperApl {
    private final ProdutoGateway prodGateway;

    private final UsuarioGateway usuGateway;

    public CarrinhoMapperApl(ProdutoGateway prodGateway, UsuarioGateway usuGateway) {
        this.prodGateway = prodGateway;
        this.usuGateway = usuGateway;
    }

    public Carrinho toDomain(CreateCarrinhoRequest carrinhoRequest){
        Carrinho carrinho = new Carrinho();
        carrinho.setComprador(usuGateway.getById(carrinhoRequest.getComprador()).orElseThrow(() -> new RuntimeException("Comprador não encontrado")));
        List<Produtos> prods = carrinhoRequest.getProdutosIds()
                .stream()
                .map(c -> prodGateway.GetById(c)
                        .orElseThrow(() -> new RuntimeException("Produtos não encontrados")))
                .collect(Collectors.toList());

        carrinho.setItens(prods);
       return carrinho;
    }
}
