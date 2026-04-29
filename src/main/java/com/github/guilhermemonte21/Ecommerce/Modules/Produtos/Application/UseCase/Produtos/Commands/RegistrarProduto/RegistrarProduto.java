package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.RegistrarProduto;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.AcessoNegadoException;
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

public class RegistrarProduto implements IRegistrarProduto {

    private static final Logger log = LoggerFactory.getLogger(RegistrarProduto.class);

    private final ProdutoGateway gateway;
    private final ProdutoMapperApl produtoMapper;
    private final UsuarioAutenticadoGateway authGateway;
    private final EventPublisher eventPublisher;

    public RegistrarProduto(ProdutoGateway gateway, ProdutoMapperApl produtoMapper,
                            UsuarioAutenticadoGateway authGateway, EventPublisher eventPublisher) {
        this.gateway = gateway;
        this.produtoMapper = produtoMapper;
        this.authGateway = authGateway;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ProdutoResponse create(CreateProdutoRequest produtos) {
        UsuarioAutenticado user = authGateway.get();
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
        if (!"Vendedor".equals(user.getUser().getTipoUsuario())) {
            throw new AcessoNegadoException();
        }
        Produtos newProd = produtoMapper.toDomain(produtos, user.getUser().getId());
        Produtos salvo = gateway.salvar(newProd);
        log.info("Produto registrado: id={}, nome={}", salvo.getId(), salvo.getNomeProduto());

        ProdutoAlteradoEvent event = ProdutoAlteradoEvent.builder()
                .id(salvo.getId())
                .nomeProduto(salvo.getNomeProduto())
                .vendedorId(salvo.getVendedorId())
                .descricao(salvo.getDescricao())
                .preco(salvo.getPreco())
                .estoque(salvo.getEstoque())
                .tipoAlteracao("CRIADO")
                .occurredOn(OffsetDateTime.now())
                .build();
        eventPublisher.publish(event);

        return produtoMapper.toResponse(salvo);
    }
}
