package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.RemoverItemDoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class RemoverItemDoCarrinho implements IRemoverItemDoCarrinho {

    private final CarrinhoGateway gateway;
    private final UsuarioAutenticadoGateway authGateway;

    public RemoverItemDoCarrinho(CarrinhoGateway gateway, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.authGateway = authGateway;
    }

    @Override
    @Transactional
    public void removerItem(UUID idCarrinho, UUID idProduto) {
        Carrinho carrinho = gateway.getById(idCarrinho)
                .orElseThrow(() -> new CarrinhoNotFoundException(idCarrinho));
        UsuarioAutenticado user = authGateway.get();
        if (!user.getUser().getId().equals(carrinho.getComprador().getId())) {
            throw new AcessoNegadoException();
        }
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }
        gateway.deleteItem(carrinho, idProduto);
        carrinho.atualizarValorTotal();
        carrinho.atualizadoAgora();
    }
}
