package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AtualizarEstoque {

    private final ProdutoGateway gateway;

    public AtualizarEstoque(ProdutoGateway gateway) {
        this.gateway = gateway;
    }

    public Long AtualizarEstoque(UUID idUser, UUID idProduto, Long quantity) {

        Produtos produto = gateway.GetById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.getVendedor().getId().equals(idUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Usuário não é o vendedor do produto");
        }

        produto.AtualizarEstoque(quantity);

        gateway.salvar(produto);

        return produto.getEstoque();
    }
}
