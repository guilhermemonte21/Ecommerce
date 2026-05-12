package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.AddItemAoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Mappers.CarrinhoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Service.CarrinhoAuthorizationService;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoCommandGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class AddItemAoCarrinho implements IAddItemAoCarrinho {

    private final CarrinhoGateway gateway;
    private final ProdutoCommandGateway produtoGateway;
    private final CarrinhoMapperApl mapperApl;
    private final CarrinhoAuthorizationService authorizationService;

    public AddItemAoCarrinho(CarrinhoGateway gateway, ProdutoCommandGateway produtoGateway,
            CarrinhoMapperApl mapperApl, CarrinhoAuthorizationService authorizationService) {
        this.gateway = gateway;
        this.produtoGateway = produtoGateway;
        this.mapperApl = mapperApl;
        this.authorizationService = authorizationService;
    }

    @Override
    @Transactional
    public CarrinhoResponse adicionarAoCarrinho(UUID idCarrinho, UUID idProduto, Long quantidade) {
        Produtos produto = produtoGateway.getById(idProduto)
                .orElseThrow(() -> new ProdutoNotFoundException(idProduto));

        Carrinho carrinho = gateway.getById(idCarrinho)
                .orElseThrow(() -> new CarrinhoNotFoundException(idCarrinho));
        
        authorizationService.validarDono(carrinho.getCompradorId());

        carrinho.adicionarItem(produto.getId(), produto.getNomeProduto(), quantidade, produto.getPreco());
        Carrinho salvamento = gateway.save(carrinho);
        return mapperApl.domainToResponse(salvamento);
    }
}
