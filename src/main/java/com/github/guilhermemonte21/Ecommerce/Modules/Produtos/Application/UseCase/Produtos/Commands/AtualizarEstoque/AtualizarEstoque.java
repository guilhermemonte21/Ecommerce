package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.AtualizarEstoque;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Service.ProdutoAuthorizationService;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.QuantidadeInvalidaException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.ProdutoAlteradoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class AtualizarEstoque implements IAtualizarEstoque {

    private static final Logger log = LoggerFactory.getLogger(AtualizarEstoque.class);

    private final com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoCommandGateway gateway;
    private final ProdutoAuthorizationService authorizationService;
    private final EventPublisher eventPublisher;

    public AtualizarEstoque(com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoCommandGateway gateway, ProdutoAuthorizationService authorizationService, EventPublisher eventPublisher) {
        this.gateway = gateway;
        this.authorizationService = authorizationService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Long atualizarEstoque(UUID idProduto, Long quantity) {
        Produtos produto = gateway.getById(idProduto)
                .orElseThrow(() -> new ProdutoNotFoundException(idProduto));

        UsuarioAutenticado user = authorizationService.validarProprietario(produto.getVendedorId());
        String vendedorNome = user.getUser().getNome();

        if (quantity < 0) {
            throw new QuantidadeInvalidaException();
        }

        produto.atualizarEstoque(quantity);
        gateway.salvar(produto);
        log.info("Estoque atualizado: produtoId={}, novoEstoque={}", idProduto, produto.getEstoque());

        eventPublisher.publish(ProdutoAlteradoEvent.atualizado(produto, vendedorNome));

        return produto.getEstoque();
    }
}
