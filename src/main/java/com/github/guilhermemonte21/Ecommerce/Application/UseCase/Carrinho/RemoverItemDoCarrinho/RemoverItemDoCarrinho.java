package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.RemoverItemDoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoverItemDoCarrinho implements IRemoverItemDoCarrinho{
    private final CarrinhoGateway gateway;
    private final UsuarioAutenticadoGateway AuthGateway;

    public RemoverItemDoCarrinho(CarrinhoGateway gateway, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        AuthGateway = authGateway;
    }

    @Override
    @Transactional
    public void RemoverItem(UUID IdCarrinho, UUID idProduto){
        Carrinho carrinho = gateway.getById(IdCarrinho).orElseThrow(() -> new CarrinhoNotFoundException(IdCarrinho));
        UsuarioAutenticado user = AuthGateway.get();
        if(!user.getUser().getId().equals(carrinho.getComprador().getId())){
            throw new AcessoNegadoException();
        }
        if (user.getUser().getAtivo() == false){
            throw new UsuarioInativoException();
        }
        gateway.DeleteItem(carrinho,idProduto);
        carrinho.atualizarValorTotal();
        carrinho.AtualizadoAgora();

    }
}
