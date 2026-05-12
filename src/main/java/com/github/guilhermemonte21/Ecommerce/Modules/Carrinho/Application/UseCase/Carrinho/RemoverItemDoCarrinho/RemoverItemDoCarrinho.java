package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.RemoverItemDoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Service.CarrinhoAuthorizationService;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.CarrinhoNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class RemoverItemDoCarrinho implements IRemoverItemDoCarrinho {

    private final CarrinhoGateway gateway;
    private final CarrinhoAuthorizationService authorizationService;

    public RemoverItemDoCarrinho(CarrinhoGateway gateway, CarrinhoAuthorizationService authorizationService) {
        this.gateway = gateway;
        this.authorizationService = authorizationService;
    }

    @Override
    @Transactional
    public void removerItem(UUID idCarrinho, UUID idProduto) {
        Carrinho carrinho = gateway.getById(idCarrinho)
                .orElseThrow(() -> new CarrinhoNotFoundException(idCarrinho));
        
        authorizationService.validarDono(carrinho.getCompradorId());

        carrinho.removerItem(idProduto);
        gateway.save(carrinho);
    }
}
