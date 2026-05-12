package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.LimparCarrinho;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Service.CarrinhoAuthorizationService;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.CarrinhoNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class LimparCarrinho implements ILimparCarrinho {

    private final CarrinhoGateway gateway;
    private final CarrinhoAuthorizationService authorizationService;

    public LimparCarrinho(CarrinhoGateway gateway, CarrinhoAuthorizationService authorizationService) {
        this.gateway = gateway;
        this.authorizationService = authorizationService;
    }

    @Override
    @Transactional
    public void limparCarrinho(UUID idCarrinho) {
        Carrinho carrinho = gateway.getById(idCarrinho)
                .orElseThrow(() -> new CarrinhoNotFoundException(idCarrinho));
        
        authorizationService.validarDono(carrinho.getCompradorId());

        carrinho.limpar();
        gateway.save(carrinho);
    }
}
