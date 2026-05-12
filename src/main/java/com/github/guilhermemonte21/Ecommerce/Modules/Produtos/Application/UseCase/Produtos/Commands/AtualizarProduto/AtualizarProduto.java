package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.AtualizarProduto;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoCommandGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Service.ProdutoAuthorizationService;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.ProdutoAlteradoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class AtualizarProduto implements IAtualizarProduto {

    private static final Logger log = LoggerFactory.getLogger(AtualizarProduto.class);

    private final ProdutoCommandGateway gateway;
    private final ProdutoMapperApl mapperApl;
    private final ProdutoAuthorizationService authorizationService;
    private final EventPublisher eventPublisher;

    public AtualizarProduto(ProdutoCommandGateway gateway, ProdutoMapperApl mapperApl,
            ProdutoAuthorizationService authorizationService, EventPublisher eventPublisher) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
        this.authorizationService = authorizationService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ProdutoResponse atualizar(UUID idProduto, CreateProdutoRequest request) {
        Produtos produto = gateway.getById(idProduto)
                .orElseThrow(() -> new ProdutoNotFoundException(idProduto));

        UsuarioAutenticado user = authorizationService.validarProprietario(produto.getVendedorId());
        String vendedorNome = user.getUser().getNome();

        produto.aplicarAtualizacao(request.nomeProduto(), request.descricao(), request.preco(), request.estoque());

        Produtos salvo = gateway.salvar(produto);
        log.info("Produto atualizado: id={}", idProduto);

        eventPublisher.publish(ProdutoAlteradoEvent.atualizado(salvo, vendedorNome));

        return mapperApl.toResponse(salvo, vendedorNome);
    }
}
