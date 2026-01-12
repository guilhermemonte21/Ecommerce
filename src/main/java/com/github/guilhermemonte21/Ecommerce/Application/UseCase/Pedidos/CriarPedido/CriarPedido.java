package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class CriarPedido implements ICriarPedido{

    private final PedidoGateway Pedidogateway;
    private final CarrinhoGateway CarrinhoGateway;

    public CriarPedido(PedidoGateway pedidogateway, CarrinhoGateway carrinhoGateway) {
        Pedidogateway = pedidogateway;
        CarrinhoGateway = carrinhoGateway;
    }

    @Transactional
    @Override
    public Pedidos CriarPedido(UUID CarrinhoId) {
        Carrinho cart = CarrinhoGateway.getById(CarrinhoId).orElseThrow();
        Pedidos Pedido = new Pedidos();
        Pedido.setComprador(cart.getComprador());
        Pedidos salvo = Pedidogateway.save(Pedido);

        for (Produtos produtos : cart.getItens()){
            PedidoDoVendedor micro = new PedidoDoVendedor();
            micro.getProdutos().add(produtos);
            micro.setVendedor(produtos.getVendedor());
            micro.setPedido(salvo.getId());
            micro.setValor(produtos.getPreco());
            salvo.getItens().add(micro);
        }

        salvo.setPreco(salvo.getItens().stream()
                .map(PedidoDoVendedor::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        salvo.setCriadoEm(OffsetDateTime.now());

        Pedidos CompleteOrder = Pedidogateway.save(salvo);

        return CompleteOrder;
    }
}