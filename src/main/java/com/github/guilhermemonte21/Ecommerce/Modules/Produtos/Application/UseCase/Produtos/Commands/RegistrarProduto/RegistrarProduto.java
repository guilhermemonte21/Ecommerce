package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.RegistrarProduto;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoCommandGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Service.ProdutoAuthorizationService;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.ProdutoAlteradoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class RegistrarProduto implements IRegistrarProduto {

    private static final Logger log = LoggerFactory.getLogger(RegistrarProduto.class);

    private final ProdutoCommandGateway gateway;
    private final ProdutoMapperApl produtoMapper;
    private final ProdutoAuthorizationService authorizationService;
    private final EventPublisher eventPublisher;

    public RegistrarProduto(com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoCommandGateway gateway, ProdutoMapperApl produtoMapper,
                            ProdutoAuthorizationService authorizationService, EventPublisher eventPublisher) {
        this.gateway = gateway;
        this.produtoMapper = produtoMapper;
        this.authorizationService = authorizationService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ProdutoResponse create(CreateProdutoRequest request) {
        UsuarioAutenticado user = authorizationService.validarVendedorAtivo();
        String vendedorNome = user.getUser().getNome();
        
        Produtos newProd = produtoMapper.toDomain(request, user.getUser().getId());
        Produtos salvo = gateway.salvar(newProd);
        
        log.info("Produto registrado: id={}, nome={}", salvo.getId(), salvo.getNomeProduto());

        eventPublisher.publish(ProdutoAlteradoEvent.criado(salvo, vendedorNome));

        return produtoMapper.toResponse(salvo, vendedorNome);
    }
}
