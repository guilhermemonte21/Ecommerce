package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PagamentoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Transfer;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.TransferCreateParams;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StripePagamentoGatewayImpl implements PagamentoGateway {

    private static final Logger log = LoggerFactory.getLogger(StripePagamentoGatewayImpl.class);

    @Override
    @CircuitBreaker(name = "stripeService", fallbackMethod = "fallbackPagamento")
    public boolean processarPagamento(Pedidos pedido) {
        for (PedidoDoVendedor subPedido : pedido.getItens()) {
            String stripeAcc = subPedido.getVendedor().getStripeAccountId();
            if (stripeAcc == null || stripeAcc.trim().isEmpty()) {
                log.error("Vendedor {} não possui conta Stripe.", subPedido.getVendedor().getId());
                throw new RuntimeException("O vendedor " + subPedido.getVendedor().getNome()
                        + " não possui conta Stripe configurada para receber pagamentos.");
            }
        }

        try {
            long amountCents = pedido.getPreco().multiply(new BigDecimal("100")).longValue();
            String transferGroup = "ORDER_" + pedido.getId().toString();

            log.info("Iniciando pagamento para o pedido {}, valor {} centavos", pedido.getId(), amountCents);

            PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                    .setAmount(amountCents)
                    .setCurrency("brl")
                    .setTransferGroup(transferGroup)
                    .setPaymentMethod("pm_card_visa")
                    .setConfirm(true)
                    .setReturnUrl("http://localhost:8080/api/v1/pagamento/retorno")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(createParams);
            log.info("PaymentIntent criado e confirmado: {}", paymentIntent.getId());

            for (PedidoDoVendedor subPedido : pedido.getItens()) {
                long valorVendedorCents = subPedido.getValor().multiply(new BigDecimal("100")).longValue();
                String stripeAccountId = subPedido.getVendedor().getStripeAccountId();

                TransferCreateParams transferParams = TransferCreateParams.builder()
                        .setAmount(valorVendedorCents)
                        .setCurrency("brl")
                        .setDestination(stripeAccountId)
                        .setTransferGroup(transferGroup)
                        .build();

                Transfer transfer = Transfer.create(transferParams);
                log.info("Transferência de {} centavos criada para Vendedor {}: TransferID={}",
                        valorVendedorCents, stripeAccountId, transfer.getId());
            }

            return true;

        } catch (StripeException e) {
            log.error("Erro ao integrar com a Stripe: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao processar pagamento com a Stripe: " + e.getUserMessage());
        }
    }

    public boolean fallbackPagamento(Pedidos pedido, Throwable t) {
        log.error("Circuit Breaker aberto ou falha na Stripe para o pedido {}. Erro: {}", pedido.getId(), t.getMessage());
        throw new RuntimeException("Serviço de pagamento (Stripe) temporariamente indisponível. Tente novamente mais tarde.");
    }
}
