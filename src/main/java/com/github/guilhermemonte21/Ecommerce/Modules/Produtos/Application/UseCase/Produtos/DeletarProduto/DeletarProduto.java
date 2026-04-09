package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.DeletarProduto;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DeletarProduto implements IDeletarProduto {

    private static final Logger log = LoggerFactory.getLogger(DeletarProduto.class);

    private final ProdutoGateway gateway;
    private final UsuarioAutenticadoGateway authGateway;

    public DeletarProduto(ProdutoGateway gateway, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.authGateway = authGateway;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deletar(UUID id) {
        Produtos produtoById = gateway.getById(id).orElseThrow(() -> new ProdutoNotFoundException(id));
        UsuarioAutenticado user = authGateway.get();
        if (!user.getUser().getId().equals(produtoById.getVendedor().getId())) {
            throw new AcessoNegadoException();
        }
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
        gateway.delete(produtoById);
        log.info("Produto deletado: id={}", id);
    }
}
