package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoVazioException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.PedidoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.*;
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
    private final UsuarioAutenticadoGateway AuthGateway;

    public CriarPedido(PedidoGateway pedidogateway, CarrinhoGateway carrinhoGateway, PedidoMapperApl mapperApl, UsuarioAutenticadoGateway authGateway) {
        Pedidogateway = pedidogateway;
        CarrinhoGateway = carrinhoGateway;
        this.mapperApl = mapperApl;
        AuthGateway = authGateway;
    }

    @Transactional
    @Override
    public PedidoResponse CriarPedido(UUID CarrinhoId, String Endereco) {
        UsuarioAutenticado user = AuthGateway.get();
        Carrinho cart = CarrinhoGateway.getById(CarrinhoId).orElseThrow(() -> new CarrinhoNotFoundException(CarrinhoId));
        Pedidos Pedido = new Pedidos();
        Pedido.setComprador(user.getUser());
        Pedidos salvo = Pedidogateway.save(Pedido);
        if (cart.getItens() == null){
            throw new CarrinhoVazioException( );
        }

        Map<UUID, PedidoDoVendedor> orders = new HashMap<>();
        for (Produtos produto : cart.getItens()) {
            UUID vendedorId = produto.getVendedor().getId();

            PedidoDoVendedor pedido = orders.computeIfAbsent(vendedorId, id -> {
                PedidoDoVendedor novo = new PedidoDoVendedor();
                novo.setVendedor(produto.getVendedor());
                novo.setPedido(salvo.getId());
                novo.setValor(BigDecimal.ZERO);
                return novo;
            });
            pedido.getProdutos().add(produto);
            pedido.setValor(pedido.getValor().add(produto.getPreco()));
        }
        salvo.getItens().addAll(orders.values());
        salvo.setPreco(orders.values().stream()
                .map(PedidoDoVendedor::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        salvo.setEndereço(Endereco);
        salvo.setCriadoEm(OffsetDateTime.now());
        Pedidos CompleteOrder = Pedidogateway.save(salvo);
        return mapperApl.toResponse(CompleteOrder);
    }
}