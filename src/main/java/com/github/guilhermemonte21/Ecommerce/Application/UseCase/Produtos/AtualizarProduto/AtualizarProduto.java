package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarProduto;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AtualizarProduto implements IAtualizarProduto{
    private final ProdutoGateway gateway;
    private final ProdutoMapperApl mapperApl;
    private final UsuarioGateway usuarioGateway;
    private final UsuarioAutenticadoGateway AuthGateway;

    public AtualizarProduto(ProdutoGateway gateway, ProdutoMapperApl mapperApl, UsuarioGateway usuarioGateway, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
        this.usuarioGateway = usuarioGateway;
        AuthGateway = authGateway;
    }

    @Override
    public ProdutoResponse Atualizar(UUID IdProduto, CreateProdutoRequest produtos){

       Produtos ProdById = gateway.GetById(IdProduto)
                .orElseThrow(() -> new ProdutoNotFoundException(IdProduto));
        UsuarioAutenticado user = AuthGateway.get();
        if(!user.getId().equals(ProdById.getVendedor().getId())){
            throw new RuntimeException("Acesso Negado");
        }

      Produtos produtos1 = new Produtos(
              ProdById.getId(),
              produtos.nomeProduto(),
              usuarioGateway.getById(produtos.vendedor()).orElseThrow(() -> new UsuarioNotFoundException(produtos.vendedor())),
              produtos.descricao(),
              produtos.preco(),
              produtos.estoque()
      );
      Produtos salvo =  gateway.salvar(ProdById);
      return mapperApl.ToResponse(salvo);
    }
}
