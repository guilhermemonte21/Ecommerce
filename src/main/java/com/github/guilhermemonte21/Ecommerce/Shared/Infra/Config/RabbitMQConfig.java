package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper.TypePrecedence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_EVENTS = "ecommerce.events";
    public static final String EXCHANGE_DLX = "ecommerce.dlx";

    public static final String RK_PEDIDO_CRIADO = "pedido.criado";
    public static final String RK_PAGAMENTO_CONCLUIDO = "pagamento.concluido";
    public static final String RK_PEDIDO_CANCELADO = "pedido.cancelado";

    public static final String QUEUE_LIMPAR_CARRINHO = "pedido.criado.limpar-carrinho";
    public static final String QUEUE_CONFIRMAR_PAGAMENTO = "pagamento.concluido.atualizar-pedido";
    public static final String QUEUE_ROLLBACK_ESTOQUE = "pedido.cancelado.rollback-estoque";
    public static final String QUEUE_LIMPAR_CARRINHO_DLQ = "pedido.criado.limpar-carrinho.dlq";
    public static final String QUEUE_CONFIRMAR_PAGAMENTO_DLQ = "pagamento.concluido.atualizar-pedido.dlq";
    public static final String QUEUE_ROLLBACK_ESTOQUE_DLQ = "pedido.cancelado.rollback-estoque.dlq";

    public static final String QUEUE_NOTIF_PEDIDO_CRIADO = "pedido.criado.notificacao";
    public static final String QUEUE_NOTIF_PAGAMENTO_CONCLUIDO = "pagamento.concluido.notificacao";
    public static final String QUEUE_NOTIF_PEDIDO_CANCELADO = "pedido.cancelado.notificacao";
    public static final String QUEUE_NOTIF_PEDIDO_CRIADO_DLQ = "pedido.criado.notificacao.dlq";
    public static final String QUEUE_NOTIF_PAGAMENTO_CONCLUIDO_DLQ = "pagamento.concluido.notificacao.dlq";
    public static final String QUEUE_NOTIF_PEDIDO_CANCELADO_DLQ = "pedido.cancelado.notificacao.dlq";

    @Bean
    public TopicExchange eventsExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_EVENTS).durable(true).build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_DLX).durable(true).build();
    }

    private Queue buildQueueWithDlx(String queueName, String dlqName) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", EXCHANGE_DLX);
        args.put("x-dead-letter-routing-key", dlqName);
        return QueueBuilder.durable(queueName).withArguments(args).build();
    }

    private Queue buildDlq(String dlqName) {
        return QueueBuilder.durable(dlqName).build();
    }

    @Bean
    public Queue limparCarrinhoQueue() {
        return buildQueueWithDlx(QUEUE_LIMPAR_CARRINHO, QUEUE_LIMPAR_CARRINHO_DLQ);
    }

    @Bean
    public Queue confirmarPagamentoQueue() {
        return buildQueueWithDlx(QUEUE_CONFIRMAR_PAGAMENTO, QUEUE_CONFIRMAR_PAGAMENTO_DLQ);
    }

    @Bean
    public Queue rollbackEstoqueQueue() {
        return buildQueueWithDlx(QUEUE_ROLLBACK_ESTOQUE, QUEUE_ROLLBACK_ESTOQUE_DLQ);
    }

    @Bean
    public Queue limparCarrinhoDlq() {
        return buildDlq(QUEUE_LIMPAR_CARRINHO_DLQ);
    }

    @Bean
    public Queue confirmarPagamentoDlq() {
        return buildDlq(QUEUE_CONFIRMAR_PAGAMENTO_DLQ);
    }

    @Bean
    public Queue rollbackEstoqueDlq() {
        return buildDlq(QUEUE_ROLLBACK_ESTOQUE_DLQ);
    }

    @Bean
    public Queue notifPedidoCriadoQueue() {
        return buildQueueWithDlx(QUEUE_NOTIF_PEDIDO_CRIADO, QUEUE_NOTIF_PEDIDO_CRIADO_DLQ);
    }

    @Bean
    public Queue notifPagamentoConcluidoQueue() {
        return buildQueueWithDlx(QUEUE_NOTIF_PAGAMENTO_CONCLUIDO, QUEUE_NOTIF_PAGAMENTO_CONCLUIDO_DLQ);
    }

    @Bean
    public Queue notifPedidoCanceladoQueue() {
        return buildQueueWithDlx(QUEUE_NOTIF_PEDIDO_CANCELADO, QUEUE_NOTIF_PEDIDO_CANCELADO_DLQ);
    }

    @Bean
    public Queue notifPedidoCriadoDlq() {
        return buildDlq(QUEUE_NOTIF_PEDIDO_CRIADO_DLQ);
    }

    @Bean
    public Queue notifPagamentoConcluidoDlq() {
        return buildDlq(QUEUE_NOTIF_PAGAMENTO_CONCLUIDO_DLQ);
    }

    @Bean
    public Queue notifPedidoCanceladoDlq() {
        return buildDlq(QUEUE_NOTIF_PEDIDO_CANCELADO_DLQ);
    }

    @Bean
    public Binding bindingLimparCarrinho(Queue limparCarrinhoQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(limparCarrinhoQueue)
                .to(eventsExchange)
                .with(RK_PEDIDO_CRIADO);
    }

    @Bean
    public Binding bindingConfirmarPagamento(Queue confirmarPagamentoQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(confirmarPagamentoQueue)
                .to(eventsExchange)
                .with(RK_PAGAMENTO_CONCLUIDO);
    }

    @Bean
    public Binding bindingRollbackEstoque(Queue rollbackEstoqueQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(rollbackEstoqueQueue)
                .to(eventsExchange)
                .with(RK_PEDIDO_CANCELADO);
    }

    @Bean
    public Binding bindingNotifPedidoCriado(Queue notifPedidoCriadoQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(notifPedidoCriadoQueue)
                .to(eventsExchange)
                .with(RK_PEDIDO_CRIADO);
    }

    @Bean
    public Binding bindingNotifPagamentoConcluido(Queue notifPagamentoConcluidoQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(notifPagamentoConcluidoQueue)
                .to(eventsExchange)
                .with(RK_PAGAMENTO_CONCLUIDO);
    }

    @Bean
    public Binding bindingNotifPedidoCancelado(Queue notifPedidoCanceladoQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(notifPedidoCanceladoQueue)
                .to(eventsExchange)
                .with(RK_PEDIDO_CANCELADO);
    }

    @Bean
    public Binding bindingLimparCarrinhoDlq(Queue limparCarrinhoDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(limparCarrinhoDlq)
                .to(deadLetterExchange)
                .with(QUEUE_LIMPAR_CARRINHO_DLQ);
    }

    @Bean
    public Binding bindingConfirmarPagamentoDlq(Queue confirmarPagamentoDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(confirmarPagamentoDlq)
                .to(deadLetterExchange)
                .with(QUEUE_CONFIRMAR_PAGAMENTO_DLQ);
    }

    @Bean
    public Binding bindingRollbackEstoqueDlq(Queue rollbackEstoqueDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(rollbackEstoqueDlq)
                .to(deadLetterExchange)
                .with(QUEUE_ROLLBACK_ESTOQUE_DLQ);
    }

    @Bean
    public Binding bindingNotifPedidoCriadoDlq(Queue notifPedidoCriadoDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(notifPedidoCriadoDlq)
                .to(deadLetterExchange)
                .with(QUEUE_NOTIF_PEDIDO_CRIADO_DLQ);
    }

    @Bean
    public Binding bindingNotifPagamentoConcluidoDlq(Queue notifPagamentoConcluidoDlq,
            DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(notifPagamentoConcluidoDlq)
                .to(deadLetterExchange)
                .with(QUEUE_NOTIF_PAGAMENTO_CONCLUIDO_DLQ);
    }

    @Bean
    public Binding bindingNotifPedidoCanceladoDlq(Queue notifPedidoCanceladoDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(notifPedidoCanceladoDlq)
                .to(deadLetterExchange)
                .with(QUEUE_NOTIF_PEDIDO_CANCELADO_DLQ);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(mapper);
        converter.setTypePrecedence(TypePrecedence.INFERRED);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
