package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho.CreateCarrinhoDTO.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
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

    public Carrinho createRequestToDomain(CreateCarrinhoRequest carrinhoRequest, UUID idComprador) {
        Carrinho carrinho = new Carrinho();
        carrinho.setComprador(usuGateway.getById(idComprador)
                .orElseThrow(() -> new UsuarioNotFoundException(idComprador)));
        List<Produtos> prods = carrinhoRequest.getProdutosIds()
                .stream()
                .map(c -> prodGateway.getById(c)
                        .orElseThrow(() -> new ProdutoNotFoundException(c)))
                .collect(Collectors.toList());
        carrinho.setItens(prods);
        return carrinho;
    }

    public CarrinhoResponse domainToResponse(Carrinho carrinho) {
        return new CarrinhoResponse(
                carrinho.getId(),
                carrinho.getItens().stream().map(Produtos::getNomeProduto).toList(),
                carrinho.getComprador().getNome(),
                carrinho.getValorTotal(),
                carrinho.getAtualizadoEm()
        );
    }
}
