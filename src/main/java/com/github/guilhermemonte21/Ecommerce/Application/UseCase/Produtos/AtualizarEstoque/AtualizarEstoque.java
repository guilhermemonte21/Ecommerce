package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarEstoque;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.QuantidadeInvalidaException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AtualizarEstoque implements IAtualizarEstoque{

    private final ProdutoGateway gateway;

    public AtualizarEstoque(ProdutoGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Long AtualizarEstoque(UUID idProduto, Long quantity) {

        Produtos produto = gateway.GetById(idProduto)
                .orElseThrow(() -> new ProdutoNotFoundException(idProduto));

        if (quantity < 0){
            throw new QuantidadeInvalidaException();
        }

        produto.AtualizarEstoque(quantity);

        gateway.salvar(produto);

        return produto.getEstoque();
    }
}
