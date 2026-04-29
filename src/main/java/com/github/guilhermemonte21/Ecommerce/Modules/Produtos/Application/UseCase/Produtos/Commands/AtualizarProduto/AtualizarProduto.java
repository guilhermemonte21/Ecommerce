package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.AtualizarProduto;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.ProdutoAlteradoEvent;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import java.util.UUID;

public class AtualizarProduto implements IAtualizarProduto {

    private static final Logger log = LoggerFactory.getLogger(AtualizarProduto.class);

    private final ProdutoGateway gateway;
    private final ProdutoMapperApl mapperApl;
    private final UsuarioAutenticadoGateway authGateway;
    private final EventPublisher eventPublisher;

    public AtualizarProduto(ProdutoGateway gateway, ProdutoMapperApl mapperApl,
                            UsuarioAutenticadoGateway authGateway, EventPublisher eventPublisher) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
        this.authGateway = authGateway;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ProdutoResponse atualizar(UUID idProduto, CreateProdutoRequest produtos) {
        Produtos prodById = gateway.getById(idProduto)
                .orElseThrow(() -> new ProdutoNotFoundException(idProduto));
        UsuarioAutenticado user = authGateway.get();
        if (!user.getUser().getId().equals(prodById.getVendedorId())) {
            throw new AcessoNegadoException();
        }
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }

        prodById.setNomeProduto(produtos.nomeProduto());
        prodById.setDescricao(produtos.descricao());
        prodById.setPreco(produtos.preco());
        prodById.setEstoque(produtos.estoque());

        Produtos salvo = gateway.salvar(prodById);
        log.info("Produto atualizado: id={}", idProduto);

        ProdutoAlteradoEvent event = ProdutoAlteradoEvent.builder()
                .id(salvo.getId())
                .nomeProduto(salvo.getNomeProduto())
                .vendedorId(salvo.getVendedorId())
                .descricao(salvo.getDescricao())
                .preco(salvo.getPreco())
                .estoque(salvo.getEstoque())
                .tipoAlteracao("ATUALIZADO")
                .occurredOn(OffsetDateTime.now())
                .build();
        eventPublisher.publish(event);

        return mapperApl.toResponse(salvo);
    }
}
