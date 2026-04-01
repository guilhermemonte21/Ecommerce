package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PagamentoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Domain.Enum.StatusPedido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Pagamento implements IPagamento {

    private static final Logger log = LoggerFactory.getLogger(Pagamento.class);

    private final PedidoGateway pedidoGateway;
    private final ProdutoGateway produtoGateway;
    private final PagamentoGateway pagamentoGateway;

    public Pagamento(PedidoGateway pedidoGateway, ProdutoGateway produtoGateway, PagamentoGateway pagamentoGateway) {
        this.pedidoGateway = pedidoGateway;
        this.produtoGateway = produtoGateway;
        this.pagamentoGateway = pagamentoGateway;
    }

    @Override
    @Transactional
    public Boolean pagar(UUID idPedido) {
        Pedidos pedido = pedidoGateway.getById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(idPedido));

        log.info("Iniciando processo de pagamento no Gateway (Stripe) para o pedido {}", idPedido);
        boolean sucesso = pagamentoGateway.processarPagamento(pedido);

        if (sucesso) {
            log.info("Pagamento aprovado. Atualizando status do pedido {}", idPedido);
            pedido.confirmarPagamento();
            pedidoGateway.save(pedido);
            return true;
        }

        log.error("Pagamento não foi aprovado no Gateway para o pedido {}", idPedido);
        return false;
    }

    @Override
    @Transactional
    public void cancelarPagamento(UUID idPedido) {
        Pedidos pedido = pedidoGateway.getById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(idPedido));
        if (pedido.getStatus() == StatusPedido.ENTREGUE || pedido.getStatus() == StatusPedido.CANCELADO
                || pedido.getStatus() == StatusPedido.ENVIADO) {
            throw new IllegalStateException("Pedido ja foi entregue ou cancelado");
        }

        Map<UUID, Long> quantidadePorProduto = new HashMap<>();
        for (PedidoDoVendedor subPedido : pedido.getItens()) {
            for (Produtos produto : subPedido.getProdutos()) {
                quantidadePorProduto.merge(produto.getId(), 1L, Long::sum);
            }
        }

        for (Map.Entry<UUID, Long> entry : quantidadePorProduto.entrySet()) {
            produtoGateway.getById(entry.getKey()).ifPresent(p -> {
                p.atualizarEstoque(entry.getValue());
                produtoGateway.salvar(p);
            });
        }

        pedido.cancelar();
        pedidoGateway.save(pedido);
    }
}
