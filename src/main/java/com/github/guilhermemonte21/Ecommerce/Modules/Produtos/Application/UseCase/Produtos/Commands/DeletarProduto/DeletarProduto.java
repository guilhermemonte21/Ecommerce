package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.DeletarProduto;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Service.ProdutoAuthorizationService;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.ProdutoAlteradoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class DeletarProduto implements IDeletarProduto {

    private static final Logger log = LoggerFactory.getLogger(DeletarProduto.class);

    private final com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoCommandGateway gateway;
    private final ProdutoAuthorizationService authorizationService;
    private final EventPublisher eventPublisher;

    public DeletarProduto(com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoCommandGateway gateway, ProdutoAuthorizationService authorizationService, EventPublisher eventPublisher) {
        this.gateway = gateway;
        this.authorizationService = authorizationService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        Produtos produto = gateway.getById(id).orElseThrow(() -> new ProdutoNotFoundException(id));
        
        authorizationService.validarProprietario(produto.getVendedorId());

        gateway.delete(produto);
        log.info("Produto deletado: id={}", id);

        eventPublisher.publish(ProdutoAlteradoEvent.deletado(id));
    }
}
