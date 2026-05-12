package com.github.guilhermemonte21.Ecommerce.Modules.Pagamento.Application.UseCase.Pagamento;

import com.github.guilhermemonte21.Ecommerce.Modules.Pagamento.Application.Gateway.PagamentoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Service.PedidoAuthorizationService;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PagamentoConcluidoEvent;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PedidoCanceladoEvent;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Enum.MotivoCancelamentoPedido;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.PedidoNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class Pagamento implements IPagamento {

    private static final Logger log = LoggerFactory.getLogger(Pagamento.class);

    private final PedidoGateway pedidoGateway;
    private final PagamentoGateway pagamentoGateway;
    private final EventPublisher eventPublisher;
    private final PedidoAuthorizationService authorizationService;
    private final UsuarioGateway usuarioGateway;

    public Pagamento(PedidoGateway pedidoGateway, PagamentoGateway pagamentoGateway,
            EventPublisher eventPublisher, PedidoAuthorizationService authorizationService,
            UsuarioGateway usuarioGateway) {
        this.pedidoGateway = pedidoGateway;
        this.pagamentoGateway = pagamentoGateway;
        this.eventPublisher = eventPublisher;
        this.authorizationService = authorizationService;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    @Transactional
    public Boolean pagar(UUID idPedido) {
        Pedidos pedido = pedidoGateway.getById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(idPedido));

        authorizationService.validarComprador(pedido.getCompradorId());

        log.info("Iniciando processo de pagamento no Gateway (Stripe) para o pedido {}", idPedido);
        boolean sucesso = pagamentoGateway.processarPagamento(pedido);

        Usuarios comprador = usuarioGateway.getById(pedido.getCompradorId()).orElse(null);
        String nome = comprador != null ? comprador.getNome() : "Cliente";
        String email = comprador != null ? comprador.getEmail() : "";

        if (sucesso) {
            log.info("Pagamento aprovado. Publicando PagamentoConcluidoEvent para o pedido {}", idPedido);
            eventPublisher.publish(new PagamentoConcluidoEvent(idPedido, nome, email));
            return true;
        }

        log.error("Pagamento não foi aprovado no Gateway para o pedido {}. Publicando cancelamento para Rollback.", idPedido);

        eventPublisher.publish(PedidoCanceladoEvent.por(
            idPedido, 
            MotivoCancelamentoPedido.FALHA_PAGAMENTO, 
            nome, email, 
            pedido.coletarItensParaRollback()
        ));
        
        pedido.cancelar();
        pedidoGateway.save(pedido);
        return false;
    }

    @Override
    @Transactional
    public void cancelarPagamento(UUID idPedido) {
        Pedidos pedido = pedidoGateway.getById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(idPedido));

        authorizationService.validarComprador(pedido.getCompradorId());

        log.info("Publicando PedidoCanceladoEvent para o pedido {}", idPedido);

        Usuarios comprador = usuarioGateway.getById(pedido.getCompradorId()).orElse(null);
        String nome = comprador != null ? comprador.getNome() : "Cliente";
        String email = comprador != null ? comprador.getEmail() : "";

        eventPublisher.publish(PedidoCanceladoEvent.por(
            idPedido, 
            MotivoCancelamentoPedido.CANCELADO_PELO_USUARIO, 
            nome, email, 
            pedido.coletarItensParaRollback()
        ));
        
        pedido.cancelar();
        pedidoGateway.save(pedido);
    }
}
