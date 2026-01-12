package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarProduto;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AtualizarProduto implements IAtualizarProduto{
    private final ProdutoGateway gateway;
    private final ProdutoMapperApl mapperApl;
    private final UsuarioGateway usuarioGateway;

    public AtualizarProduto(ProdutoGateway gateway, ProdutoMapperApl mapperApl, UsuarioGateway usuarioGateway) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public ProdutoResponse Atualizar(UUID IdProduto, CreateProdutoRequest produtos){

       Produtos ProdById = gateway.GetById(IdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

      Produtos produtos1 = new Produtos(
              ProdById.getId(),
              produtos.getNomeProduto(),
              usuarioGateway.getById(produtos.getVendedor()).orElseThrow(() -> new RuntimeException("Produto não encontrado")),
              produtos.getDescricao(),
              produtos.getPreco(),
              produtos.getEstoque()
      );
      Produtos salvo =  gateway.salvar(ProdById);
      return mapperApl.ToResponse(salvo);
    }
}
