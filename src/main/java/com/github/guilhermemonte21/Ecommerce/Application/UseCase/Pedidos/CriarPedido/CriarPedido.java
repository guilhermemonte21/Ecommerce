package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;

import java.util.UUID;

public class CriarPedido implements ICriarPedido{

    private final PedidoGateway Pedidogateway;
    private final CarrinhoGateway CarrinhoGateway;
    private final ProdutoGateway ProdutosGateway;

    public CriarPedido(PedidoGateway pedidogateway, CarrinhoGateway carrinhoGateway, ProdutoGateway produtosGateway) {
        Pedidogateway = pedidogateway;
        CarrinhoGateway = carrinhoGateway;
        ProdutosGateway = produtosGateway;
    }

    @Override
    public Pedidos CriarPedido(UUID CarrinhoId) {
        Carrinho cart = CarrinhoGateway.getById(CarrinhoId).orElseThrow();


        Pedidos Pedido = new Pedidos();
        Pedido.setPreco(cart.getValorTotal());
        Pedido.setComprador(cart.getComprador());
        Pedido.setItens(cart.getItens());

        Pedidogateway.save(Pedido);
        return Pedido;
    }
}
