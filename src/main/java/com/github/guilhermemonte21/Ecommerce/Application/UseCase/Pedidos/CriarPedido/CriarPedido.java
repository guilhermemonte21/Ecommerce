package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioInativoException;
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
import java.util.stream.Collectors;

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
    public PedidoResponse CriarPedido(UUID CarrinhoId, String Endereço) {
        UsuarioAutenticado user = AuthGateway.get();
        if (user.getUser().getAtivo() == false){
            throw new UsuarioInativoException();
        }
        Carrinho cart = CarrinhoGateway.getById(CarrinhoId).orElseThrow(() -> new CarrinhoNotFoundException(CarrinhoId));
        Pedidos Pedido = new Pedidos();
        Pedido.setComprador(user.getUser());
        Pedidos salvo = Pedidogateway.save(Pedido);

        Map<UUID, PedidoDoVendedor> orders = new HashMap<>();
        for (Produtos produto : cart.getItens()) {
            UUID vendedorId = produto.getVendedor().getId();

            if (orders.containsKey(vendedorId)) {

                PedidoDoVendedor pedidoExistente = orders.get(vendedorId);
                pedidoExistente.getProdutos().add(produto);
                pedidoExistente.setValor(pedidoExistente.getValor().add(produto.getPreco()));
            }
                PedidoDoVendedor novoPedido = new PedidoDoVendedor();
                novoPedido.getProdutos().add(produto);
                novoPedido.setVendedor(produto.getVendedor());
                novoPedido.setPedido(salvo.getId());
                novoPedido.setValor(produto.getPreco());

                orders.put(vendedorId, novoPedido);
        }

        salvo.getItens().addAll(orders.values());

        salvo.setPreco(salvo.getItens().stream()
                .map(PedidoDoVendedor::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        salvo.setCriadoEm(OffsetDateTime.now());

        Pedidos CompleteOrder = Pedidogateway.save(salvo);

        PedidoResponse Dto = mapperApl.toResponse(CompleteOrder);
        return Dto;
    }




}