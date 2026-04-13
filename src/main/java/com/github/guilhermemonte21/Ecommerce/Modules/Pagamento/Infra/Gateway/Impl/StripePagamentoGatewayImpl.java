package com.github.guilhermemonte21.Ecommerce.Modules.Pagamento.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Modules.Pagamento.Application.Gateway.PagamentoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Transfer;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.TransferCreateParams;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioNotFoundException;

import java.math.BigDecimal;

@Component
public class StripePagamentoGatewayImpl implements PagamentoGateway {

    private static final Logger log = LoggerFactory.getLogger(StripePagamentoGatewayImpl.class);
    
    private final UsuarioGateway usuarioGateway;

    public StripePagamentoGatewayImpl(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    @CircuitBreaker(name = "stripeService", fallbackMethod = "fallbackPagamento")
    @Retry(name = "stripeRetry", fallbackMethod = "fallbackPagamento")
    public boolean processarPagamento(Pedidos pedido) {
        for (PedidoDoVendedor subPedido : pedido.getItens()) {
            Usuarios vendedor = usuarioGateway.getById(subPedido.getVendedorId())
                    .orElseThrow(() -> new UsuarioNotFoundException(subPedido.getVendedorId()));
            String stripeAcc = vendedor.getStripeAccountId();
            if (stripeAcc == null || stripeAcc.trim().isEmpty()) {
                log.error("Vendedor {} não possui conta Stripe.", vendedor.getId());

                throw new IllegalStateException("O vendedor " + vendedor.getNome()
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
                Usuarios vendedor = usuarioGateway.getById(subPedido.getVendedorId())
                        .orElseThrow(() -> new UsuarioNotFoundException(subPedido.getVendedorId()));
                String stripeAccountId = vendedor.getStripeAccountId();

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
            log.warn("Erro transiente ao integrar com a Stripe (tentará novamente): {}", e.getMessage());
            throw new RuntimeException("Falha ao processar pagamento com a Stripe: " + e.getUserMessage(), e);
        }
    }

    public boolean fallbackPagamento(Pedidos pedido, Throwable t) {
        log.error("Circuit Breaker aberto ou retries esgotados para o pedido {}. Erro: {}", pedido.getId(),
                t.getMessage());
        throw new RuntimeException(
                "Serviço de pagamento (Stripe) temporariamente indisponível. Tente novamente mais tarde.");
    }
}
