package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.LimparCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LimparCarrinho implements ILimparCarrinho{
    private final CarrinhoGateway gateway;
    private final UsuarioAutenticadoGateway AuthGateway;

    public LimparCarrinho(CarrinhoGateway gateway, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        AuthGateway = authGateway;
    }

    @Override
    public void LimparCarrinho( UUID IdCarrinho) {
        Carrinho carrinho = gateway.getById(IdCarrinho)
                .orElseThrow(() -> new CarrinhoNotFoundException(IdCarrinho));
        UsuarioAutenticado user = AuthGateway.get();
        if(!user.getId().equals(carrinho.getComprador().getId())){
            throw new RuntimeException("Acesso Negado");
        }

        gateway.LimparCarrinho(carrinho);
    }
}
