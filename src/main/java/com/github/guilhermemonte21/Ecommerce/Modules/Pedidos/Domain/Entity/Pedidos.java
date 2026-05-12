package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.CarrinhoItem;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedidos {
    private UUID id;
    private UUID compradorId;
    private List<PedidoDoVendedor> itens = new ArrayList<>();
    private BigDecimal preco;
    private String endereco;
    private StatusPedido status = StatusPedido.PENDENTE;
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    public void syncStatus() {
        if (this.itens.stream().allMatch(p -> p.getStatus() == StatusPedido.ENTREGUE)) {
            this.status = StatusPedido.ENTREGUE;
        } else if (this.itens.stream().allMatch(p -> p.getStatus() == StatusPedido.ENVIADO)) {
            this.status = StatusPedido.ENVIADO;
        } else if (this.itens.stream().allMatch(p -> p.getStatus() == StatusPedido.APROVADO)) {
            this.status = StatusPedido.APROVADO;
        } else if (this.itens.stream().allMatch(p -> p.getStatus() == StatusPedido.CANCELADO)) {
            this.status = StatusPedido.CANCELADO;
        } else {
            this.status = StatusPedido.PENDENTE;
        }
    }

    public java.util.Map<UUID, Long> coletarItensParaRollback() {
        java.util.Map<UUID, Long> itensRollback = new java.util.HashMap<>();
        this.getItens().forEach(item -> itensRollback.merge(item.getProdutoId(), item.getQuantidade(), Long::sum));
        return itensRollback;
    }

    public void confirmarPagamento() {
        this.status = StatusPedido.APROVADO;
        for (PedidoDoVendedor item : this.itens) {
            item.setStatus(StatusPedido.APROVADO);
        }
    }

    public void cancelar() {
        if (this.status == StatusPedido.ENTREGUE
                || this.status == StatusPedido.CANCELADO
                || this.status == StatusPedido.ENVIADO) {
            throw new com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.PedidoNaoCancelavelException(
                    this.id, this.status);
        }
        this.status = StatusPedido.CANCELADO;
        for (PedidoDoVendedor item : this.itens) {
            item.setStatus(StatusPedido.CANCELADO);
        }
    }

    public void changeToCancelado() {
        this.status = StatusPedido.CANCELADO;
    }

    public void changeToEnviado() {
        this.status = StatusPedido.ENVIADO;
    }

    public void changeToEntregue() {
        this.status = StatusPedido.ENTREGUE;
    }

    /**
     * Monta um pedido completo a partir dos itens do carrinho, produtos reservados e contas Stripe dos vendedores.
     *
     * @param compradorId       ID do comprador autenticado
     * @param itensCarrinho     lista de itens do carrinho
     * @param produtos          mapa de produtoId → Produtos já com estoque reservado
     * @param stripeAccounts    mapa de vendedorId → stripeAccountId
     * @param endereco          endereço de entrega
     * @return pedido montado, pronto para persistência
     */
    public static Pedidos criarDe(UUID compradorId,
                                   List<CarrinhoItem> itensCarrinho,
                                  Map<UUID, Produtos> produtos,
                                   Map<UUID, String> stripeAccounts,
                                   String endereco) {
        Pedidos pedido = new Pedidos();
        pedido.setCompradorId(compradorId);
        pedido.setEndereco(endereco);
        pedido.setCriadoEm(java.time.OffsetDateTime.now());

       List<PedidoDoVendedor> itens = new ArrayList<>();
        for (CarrinhoItem item : itensCarrinho) {
           Produtos produto = produtos.get(item.getProdutoId());
            UUID vendedorId = produto.getVendedorId();

            itens.add(PedidoDoVendedor.builder()
                    .vendedorId(vendedorId)
                    .pedido(pedido)
                    .produtoId(item.getProdutoId())
                    .nomeProduto(produto.getNomeProduto())
                    .precoUnitario(produto.getPreco())
                    .quantidade(item.getQuantidade())
                    .valor(produto.getPreco().multiply(java.math.BigDecimal.valueOf(item.getQuantidade())))
                    .status(StatusPedido.PENDENTE)
                    .stripeAccountId(stripeAccounts.get(vendedorId))
                    .build());
        }

        pedido.setItens(itens);
        pedido.setPreco(itens.stream()
                .map(PedidoDoVendedor::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return pedido;
    }
}
