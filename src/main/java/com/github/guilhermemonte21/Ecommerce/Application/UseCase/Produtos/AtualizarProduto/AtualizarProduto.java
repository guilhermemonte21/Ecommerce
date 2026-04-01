package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarProduto;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class AtualizarProduto implements IAtualizarProduto {

    private static final Logger log = LoggerFactory.getLogger(AtualizarProduto.class);

    private final ProdutoGateway gateway;
    private final ProdutoMapperApl mapperApl;
    private final UsuarioAutenticadoGateway authGateway;

    public AtualizarProduto(ProdutoGateway gateway, ProdutoMapperApl mapperApl,
            UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
        this.authGateway = authGateway;
    }

    @Override
    @Transactional
    public ProdutoResponse atualizar(UUID idProduto, CreateProdutoRequest produtos) {
        Produtos prodById = gateway.getById(idProduto)
                .orElseThrow(() -> new ProdutoNotFoundException(idProduto));
        UsuarioAutenticado user = authGateway.get();
        if (!user.getUser().getId().equals(prodById.getVendedor().getId())) {
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
        return mapperApl.toResponse(salvo);
    }
}
