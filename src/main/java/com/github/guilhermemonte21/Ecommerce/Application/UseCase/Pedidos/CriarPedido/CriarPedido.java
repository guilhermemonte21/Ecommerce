package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoDoVendedorGateway;
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
    private final PedidoDoVendedorGateway pedidoDoVendedorGateway;

    public CriarPedido(PedidoGateway pedidogateway, CarrinhoGateway carrinhoGateway, PedidoDoVendedorGateway pedidoDoVendedorGateway) {
        Pedidogateway = pedidogateway;
        CarrinhoGateway = carrinhoGateway;
        this.pedidoDoVendedorGateway = pedidoDoVendedorGateway;
    }

    @Transactional
    @Override
    public Pedidos CriarPedido(UUID CarrinhoId) {
        Carrinho cart = CarrinhoGateway.getById(CarrinhoId).orElseThrow();
        Pedidos Pedido = new Pedidos();

        Pedido.setComprador(cart.getComprador());

        List<PedidoDoVendedor> lista = new ArrayList<>();
        for (Produtos produtos : cart.getItens()){
            PedidoDoVendedor micro = new PedidoDoVendedor();
            micro.getProdutos().add(produtos);
            micro.setVendedor(produtos.getVendedor());
            micro.setPedido(Pedido);
            micro.setValor(produtos.getPreco());
            lista.add(micro);
        }

        Pedido.setPreco(lista.stream()
                .map(PedidoDoVendedor::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Pedido.setItens(lista);
        Pedido.setCriadoEm(OffsetDateTime.now());
        Pedidos salvo = Pedidogateway.save(Pedido);

        return salvo;
    }
}