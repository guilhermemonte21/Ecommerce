package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.LimparCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;

import java.util.UUID;

public class LimparCarrinho implements ILimparCarrinho {

    private final CarrinhoGateway gateway;
    private final UsuarioAutenticadoGateway authGateway;

    public LimparCarrinho(CarrinhoGateway gateway, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.authGateway = authGateway;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void limparCarrinho(UUID idCarrinho) {
        Carrinho carrinho = gateway.getById(idCarrinho)
                .orElseThrow(() -> new CarrinhoNotFoundException(idCarrinho));
        UsuarioAutenticado user = authGateway.get();
        if (!user.getUser().getId().equals(carrinho.getComprador().getId())) {
            throw new AcessoNegadoException();
        }
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
        carrinho.limpar();
        gateway.save(carrinho);
    }
}
