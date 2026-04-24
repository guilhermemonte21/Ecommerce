package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Event.Consumer;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PedidoCriadoEvent;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LimparCarrinhoConsumer {

    private static final Logger log = LoggerFactory.getLogger(LimparCarrinhoConsumer.class);

    private final CarrinhoGateway carrinhoGateway;

    public LimparCarrinhoConsumer(CarrinhoGateway carrinhoGateway) {
        this.carrinhoGateway = carrinhoGateway;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_LIMPAR_CARRINHO)
    @Transactional
    public void onPedidoCriado(PedidoCriadoEvent event) {
        log.info("Recebido PedidoCriadoEvent: pedidoId={}, compradorId={}",
                event.getPedidoId(), event.getCompradorId());

        Carrinho carrinho = carrinhoGateway.getByDono(event.getCompradorId());

        if (carrinho == null || carrinho.getItens() == null || carrinho.getItens().isEmpty()) {
            log.warn("Carrinho do comprador {} já está vazio ou não encontrado. Nada a fazer.",
                    event.getCompradorId());
            return;
        }

        carrinho.limpar();
        carrinhoGateway.save(carrinho);

        log.info("Carrinho do comprador {} limpo com sucesso após criação do pedido {}",
                event.getCompradorId(), event.getPedidoId());
    }
}
