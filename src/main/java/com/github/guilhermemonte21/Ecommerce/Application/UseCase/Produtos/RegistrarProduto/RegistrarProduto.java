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
import org.springframework.stereotype.Service;
@Service
public class RegistrarProduto implements IRegistrarProduto{

    private final ProdutoGateway gateway;
    private final ProdutoMapperApl produtoMapper;
    private final UsuarioAutenticadoGateway AuthGateway;

    public RegistrarProduto(ProdutoGateway gateway, ProdutoMapperApl produtoMapper, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.produtoMapper = produtoMapper;
        AuthGateway = authGateway;
    }

    @Override
    public ProdutoResponse Create(CreateProdutoRequest produtos){
        UsuarioAutenticado user = AuthGateway.get();
        if (user.getUser().getAtivo() == false){
            throw new UsuarioInativoException();
        }
        if (user.getUser().getTipoUsuario() != "Vendedor"){
            throw new AcessoNegadoException();
        }
        Produtos newProd = produtoMapper.toDomain(produtos, user.getUser().getId());
        Produtos salvo = gateway.salvar(newProd);
        return produtoMapper.ToResponse(salvo);
    }
}
