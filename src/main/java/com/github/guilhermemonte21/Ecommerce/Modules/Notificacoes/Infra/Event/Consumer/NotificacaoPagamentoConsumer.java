package com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Infra.Event.Consumer;

import com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Application.UseCase.EnviarEmailNotificacao;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PagamentoConcluidoEvent;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificacaoPagamentoConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoPagamentoConsumer.class);

    private final EnviarEmailNotificacao enviarEmailNotificacao;
    private final UsuarioGateway usuarioGateway;
    private final PedidoGateway pedidoGateway;

    public NotificacaoPagamentoConsumer(EnviarEmailNotificacao enviarEmailNotificacao,
                                      UsuarioGateway usuarioGateway,
                                      PedidoGateway pedidoGateway) {
        this.enviarEmailNotificacao = enviarEmailNotificacao;
        this.usuarioGateway = usuarioGateway;
        this.pedidoGateway = pedidoGateway;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIF_PAGAMENTO_CONCLUIDO)
    public void onPagamentoConcluido(PagamentoConcluidoEvent event) {
        log.info("Recebido PagamentoConcluidoEvent para notificação: pedidoId={}", event.getPedidoId());

        Pedidos pedido = pedidoGateway.getById(event.getPedidoId())
                .orElse(null);

        if (pedido == null) {
            log.error("Pedido {} não encontrado para enviar notificação de pagamento", event.getPedidoId());
            return;
        }

        Usuarios usuario = usuarioGateway.getById(pedido.getCompradorId())
                .orElse(null);

        if (usuario == null) {
            log.error("Usuário {} não encontrado para enviar notificação de pagamento", pedido.getCompradorId());
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("nome", usuario.getNome());
        variables.put("pedidoId", pedido.getId());

        enviarEmailNotificacao.enviar(
                usuario.getEmail(),
                "Pagamento Confirmado! Pedido #" + pedido.getId().toString().substring(0, 8),
                "pagamento-confirmado",
                variables
        );
    }
}
