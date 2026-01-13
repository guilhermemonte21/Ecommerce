package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.PedidoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class CriarPedido implements ICriarPedido{

    private final PedidoGateway Pedidogateway;
    private final CarrinhoGateway CarrinhoGateway;
    private final PedidoMapperApl mapperApl;

    public CriarPedido(PedidoGateway pedidogateway, CarrinhoGateway carrinhoGateway, PedidoMapperApl mapperApl) {
        Pedidogateway = pedidogateway;
        CarrinhoGateway = carrinhoGateway;
        this.mapperApl = mapperApl;
    }
    @Transactional
    @Override
    public PedidoResponse CriarPedido(UUID CarrinhoId) {
        Carrinho cart = CarrinhoGateway.getById(CarrinhoId).orElseThrow(() -> new CarrinhoNotFoundException(CarrinhoId));
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

        PedidoResponse Dto = mapperApl.toResponse(CompleteOrder);
        return Dto;
    }




}