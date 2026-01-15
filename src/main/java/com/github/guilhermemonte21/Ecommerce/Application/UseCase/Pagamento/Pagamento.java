package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoDoVendedorGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.ChangePedidoStatus.ChangePedidoStatus;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Domain.Enum.StatusPedido;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class Pagamento {

    private final PedidoGateway pedidoGateway;
    private final ChangePedidoStatus change;
    private final ProdutoGateway produtoGateway;

    public Pagamento(PedidoGateway pedidoGateway, ChangePedidoStatus change, ProdutoGateway produtoGateway) {
        this.pedidoGateway = pedidoGateway;
        this.change = change;
        this.produtoGateway = produtoGateway;
    }

    public Boolean pagar(UUID IdPedido){
        Pedidos order = pedidoGateway.getById(IdPedido).orElseThrow(() -> new PedidoNotFoundException(IdPedido));

        List<PedidoDoVendedor> itens = order.getItens();
        for (PedidoDoVendedor seller : itens){
            seller.setStatus("Pago");
            for (Produtos produtos : seller.getProdutos()){
                produtos.setEstoque(produtos.getEstoque() - 1L );
                produtoGateway.salvar(produtos);
            }
        }
        change.ChangePedidosStatus(order.getId());

        pedidoGateway.save(order);
        return true;
    }
}
