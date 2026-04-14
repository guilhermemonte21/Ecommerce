package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.RemoverItemDoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class RemoverItemDoCarrinho implements IRemoverItemDoCarrinho {

    private final CarrinhoGateway gateway;
    private final UsuarioAutenticadoGateway authGateway;
    private final ProdutoGateway produtoGateway;

    public RemoverItemDoCarrinho(CarrinhoGateway gateway, UsuarioAutenticadoGateway authGateway, ProdutoGateway produtoGateway) {
        this.gateway = gateway;
        this.authGateway = authGateway;
        this.produtoGateway = produtoGateway;
    }

    @Override
    @Transactional
    public void removerItem(UUID idCarrinho, UUID idProduto) {
        Carrinho carrinho = gateway.getById(idCarrinho)
                .orElseThrow(() -> new CarrinhoNotFoundException(idCarrinho));
        UsuarioAutenticado user = authGateway.get();
        if (!user.getUser().getId().equals(carrinho.getCompradorId())) {
            throw new AcessoNegadoException();
        }
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
        Produtos produto = produtoGateway.getById(idProduto)
                .orElseThrow(() -> new ProdutoNotFoundException(idProduto));
        carrinho.removerItem(idProduto, produto.getPreco());
        gateway.save(carrinho);
    }
}
