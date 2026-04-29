package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.AtualizarEstoque;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.QuantidadeInvalidaException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.ProdutoAlteradoEvent;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;

import java.util.UUID;

public class AtualizarEstoque implements IAtualizarEstoque {

    private static final Logger log = LoggerFactory.getLogger(AtualizarEstoque.class);

    private final ProdutoGateway gateway;
    private final UsuarioAutenticadoGateway authGateway;
    private final EventPublisher eventPublisher;

    public AtualizarEstoque(ProdutoGateway gateway, UsuarioAutenticadoGateway authGateway, EventPublisher eventPublisher) {
        this.gateway = gateway;
        this.authGateway = authGateway;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Long atualizarEstoque(UUID idProduto, Long quantity) {
        Produtos produto = gateway.getById(idProduto)
                .orElseThrow(() -> new ProdutoNotFoundException(idProduto));
        UsuarioAutenticado user = authGateway.get();
        if (!user.getUser().getId().equals(produto.getVendedorId())) {
            throw new AcessoNegadoException();
        }
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
        if (quantity < 0) {
            throw new QuantidadeInvalidaException();
        }

        produto.atualizarEstoque(quantity);
        gateway.salvar(produto);
        log.info("Estoque atualizado: produtoId={}, novoEstoque={}", idProduto, produto.getEstoque());

        ProdutoAlteradoEvent event = ProdutoAlteradoEvent.builder()
                .id(produto.getId())
                .nomeProduto(produto.getNomeProduto())
                .vendedorId(produto.getVendedorId())
                .descricao(produto.getDescricao())
                .preco(produto.getPreco())
                .estoque(produto.getEstoque())
                .tipoAlteracao("ATUALIZADO")
                .occurredOn(OffsetDateTime.now())
                .build();
        eventPublisher.publish(event);

        return produto.getEstoque();
    }
}
