package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarEstoque;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.QuantidadeInvalidaException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AtualizarEstoque implements IAtualizarEstoque{

    private final ProdutoGateway gateway;
    private final UsuarioAutenticadoGateway AuthGateway;

    public AtualizarEstoque(ProdutoGateway gateway, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        AuthGateway = authGateway;
    }

    @Override
    public Long AtualizarEstoque(UUID idProduto, Long quantity) {

        Produtos produto = gateway.GetById(idProduto)
                .orElseThrow(() -> new ProdutoNotFoundException(idProduto));
        UsuarioAutenticado user = AuthGateway.get();
        if(!user.getUser().getId().equals(produto.getVendedor().getId())){
            throw new AcessoNegadoException();
        }

        if (quantity < 0){
            throw new QuantidadeInvalidaException();
        }

        produto.AtualizarEstoque(quantity);

        gateway.salvar(produto);

        return produto.getEstoque();
    }
}
