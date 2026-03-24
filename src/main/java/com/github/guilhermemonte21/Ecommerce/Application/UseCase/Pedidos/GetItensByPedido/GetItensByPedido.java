package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetItensByPedido;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoDoVendedorResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioInativoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.PedidoDoVendedorMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class GetItensByPedido implements IGetItensByPedido {

    private static final Logger log = LoggerFactory.getLogger(GetItensByPedido.class);

    private final PedidoGateway pedidoGateway;
    private final UsuarioAutenticadoGateway authGateway;
    private final PedidoDoVendedorMapperApl mapperApl;

    public GetItensByPedido(PedidoGateway pedidoGateway, UsuarioAutenticadoGateway authGateway,
                            PedidoDoVendedorMapperApl mapperApl) {
        this.pedidoGateway = pedidoGateway;
        this.authGateway = authGateway;
        this.mapperApl = mapperApl;
    }

    @Override
    public List<PedidoDoVendedorResponse> get(UUID idPedido) {
        Pedidos pedido = pedidoGateway.getById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(idPedido));
        UsuarioAutenticado user = authGateway.get();

        if (!user.getUser().getId().equals(pedido.getComprador().getId())) {
            throw new AcessoNegadoException();
        }
        if (Boolean.FALSE.equals(user.getUser().getAtivo())) {
            throw new UsuarioInativoException();
        }

        log.info("Itens do pedido {} consultados pelo usuário {}", idPedido, user.getUser().getId());
        return pedido.getItens().stream()
                .map(mapperApl::toResponse)
                .toList();
    }
}
