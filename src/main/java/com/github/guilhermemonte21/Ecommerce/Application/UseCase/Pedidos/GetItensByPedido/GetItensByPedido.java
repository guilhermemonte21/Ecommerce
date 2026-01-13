package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetItensByPedido;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetItensByPedido implements IGetItensByPedido{
    private final PedidoGateway pedidoGateway;
    private final UsuarioAutenticadoGateway AuthGateway;

    public GetItensByPedido(PedidoGateway pedidoGateway, UsuarioAutenticadoGateway authGateway) {
        this.pedidoGateway = pedidoGateway;
        AuthGateway = authGateway;
    }

    @Override
    public List<PedidoDoVendedor> get(UUID IdPedido) {
        Pedidos pedido = pedidoGateway.getById(IdPedido).orElseThrow(() -> new PedidoNotFoundException(IdPedido));
        UsuarioAutenticado user = AuthGateway.get();
        if(!user.getId().equals(pedido.getComprador().getId())){
            throw new RuntimeException("Acesso Negado");
        }
        return pedido.getItens();
    }
}
