package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Transactional
    @Override
    public Pedidos CriarPedido(UUID CarrinhoId) {
        Carrinho cart = CarrinhoGateway.getById(CarrinhoId).orElseThrow();
        Pedidos Pedido = new Pedidos();
        Pedido.setComprador(cart.getComprador());


        List<PedidosSeller> pedidosSellers = new ArrayList<>();
        for (int i = 0; i < cart.getItens().size(); i++) {
            PedidosSeller seller = new PedidosSeller();
            seller.setPedido(Pedido);

            Produtos produtos = cart.getItens().get(i);
            seller.setProdutos(cart.getItens().get(i));
            seller.setSeller(cart.getItens().get(i).getVendedor());
            seller.setValorTotal(cart.getValorTotal());

            pedidosSellers.add(seller);

        }


        Pedido.setItens(pedidosSellers);


        Pedidogateway.save(Pedido);
        return Pedido;
    }
}
