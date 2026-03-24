package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarEstoque;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.QuantidadeInvalidaException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class AtualizarEstoque implements IAtualizarEstoque {

    private static final Logger log = LoggerFactory.getLogger(AtualizarEstoque.class);

    private final ProdutoGateway gateway;
    private final UsuarioAutenticadoGateway authGateway;

    public AtualizarEstoque(ProdutoGateway gateway, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.authGateway = authGateway;
    }

    @Override
    public Long atualizarEstoque(UUID idProduto, Long quantity) {
        Produtos produto = gateway.getById(idProduto)
                .orElseThrow(() -> new ProdutoNotFoundException(idProduto));
        UsuarioAutenticado user = authGateway.get();
        if (!user.getUser().getId().equals(produto.getVendedor().getId())) {
            throw new AcessoNegadoException();
        }
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
        if (quantity < 0) {
            throw new QuantidadeInvalidaException();
        }

        produto.atualizarEstoque(quantity);
        gateway.salvar(produto);
        log.info("Estoque atualizado: produtoId={}, novoEstoque={}", idProduto, produto.getEstoque());
        return produto.getEstoque();
    }
}
