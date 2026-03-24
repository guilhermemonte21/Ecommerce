package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.RegistrarProduto;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrarProduto implements IRegistrarProduto {

    private static final Logger log = LoggerFactory.getLogger(RegistrarProduto.class);

    private final ProdutoGateway gateway;
    private final ProdutoMapperApl produtoMapper;
    private final UsuarioAutenticadoGateway authGateway;

    public RegistrarProduto(ProdutoGateway gateway, ProdutoMapperApl produtoMapper, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.produtoMapper = produtoMapper;
        this.authGateway = authGateway;
    }

    @Override
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
        return produtoMapper.toResponse(salvo);
    }
}
