package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.AddItemAoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho.CarrinhoResponse;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Mappers.CarrinhoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

public class AddItemAoCarrinho implements IAddItemAoCarrinho {

    private final CarrinhoGateway gateway;
    private final ProdutoGateway produtoGateway;
    private final CarrinhoMapperApl mapperApl;
    private final UsuarioAutenticadoGateway authGateway;

    public AddItemAoCarrinho(CarrinhoGateway gateway, ProdutoGateway produtoGateway,
            CarrinhoMapperApl mapperApl, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.produtoGateway = produtoGateway;
        this.mapperApl = mapperApl;
        this.authGateway = authGateway;
    }

    @Override
    @Transactional
    public CarrinhoResponse adicionarAoCarrinho(UUID idCarrinho, UUID idProduto, Long quantidade) {
        UsuarioAutenticado user = authGateway.get();
        Produtos produto = produtoGateway.getById(idProduto)
                .orElseThrow(() -> new ProdutoNotFoundException(idProduto));

        if (produto.getEstoque() == null || produto.getEstoque() <= 0
                || quantidade > produto.getEstoque() || quantidade <= 0) {
            throw new EstoqueInsuficienteException(produto.getNomeProduto());
        }
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }

        Carrinho carrinho = gateway.getById(idCarrinho)
                .orElseThrow(() -> new CarrinhoNotFoundException(idCarrinho));
        if (!user.getUser().getId().equals(carrinho.getCompradorId())) {
            throw new AcessoNegadoException();
        }

        carrinho.adicionarItem(produto.getId(), produto.getNomeProduto(), quantidade, produto.getPreco());
        Carrinho salvamento = gateway.save(carrinho);
        return mapperApl.domainToResponse(salvamento);
    }
}
