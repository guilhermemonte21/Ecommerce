package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.DeletarProduto;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeletarProduto implements IDeletarProduto{
    private final ProdutoGateway gateway;
    private final UsuarioAutenticadoGateway AuthGateway;

    public DeletarProduto(ProdutoGateway gateway, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        AuthGateway = authGateway;
    }

    @Override
    public void Deletar(UUID id){
        Produtos produtoById = gateway.GetById(id).orElseThrow(() -> new ProdutoNotFoundException(id));
        UsuarioAutenticado user = AuthGateway.get();
        if(!user.getId().equals(produtoById.getVendedor().getId())){
            throw new RuntimeException("Acesso Negado");
        }

        gateway.Delete(produtoById);
    }
}
