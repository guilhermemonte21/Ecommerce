package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.CarrinhoItem;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.PedidoNaoCancelavelException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PedidosTest {

    @Test
    @DisplayName("Deve sincronizar status corretamente quando todos os itens tem o mesmo status")
    void deveSincronizarStatusCorretamente() {
        Pedidos pedido = new Pedidos();
        PedidoDoVendedor item1 = PedidoDoVendedor.builder().status(StatusPedido.APROVADO).build();
        PedidoDoVendedor item2 = PedidoDoVendedor.builder().status(StatusPedido.APROVADO).build();
        pedido.setItens(List.of(item1, item2));

        pedido.syncStatus();

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.APROVADO);
    }

    @Test
    @DisplayName("Deve manter status PENDENTE se itens tiverem status mistos")
    void deveManterPendenteSeStatusMistos() {
        Pedidos pedido = new Pedidos();
        PedidoDoVendedor item1 = PedidoDoVendedor.builder().status(StatusPedido.APROVADO).build();
        PedidoDoVendedor item2 = PedidoDoVendedor.builder().status(StatusPedido.PENDENTE).build();
        pedido.setItens(List.of(item1, item2));

        pedido.syncStatus();

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.PENDENTE);
    }

    @Test
    @DisplayName("Deve cancelar pedido e todos os seus itens")
    void deveCancelarPedidoEItens() {
        Pedidos pedido = new Pedidos();
        pedido.setStatus(StatusPedido.PENDENTE);
        PedidoDoVendedor item = PedidoDoVendedor.builder().status(StatusPedido.PENDENTE).build();
        pedido.setItens(List.of(item));

        pedido.cancelar();

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.CANCELADO);
        assertThat(item.getStatus()).isEqualTo(StatusPedido.CANCELADO);
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar pedido em estado inválido")
    void deveLancarExcecaoAoCancelarPedidoInvalido() {
        Pedidos pedido = new Pedidos();
        pedido.setStatus(StatusPedido.ENTREGUE);

        assertThatThrownBy(pedido::cancelar)
                .isInstanceOf(PedidoNaoCancelavelException.class);
    }

    @Test
    @DisplayName("Deve confirmar pagamento e atualizar todos os itens")
    void deveConfirmarPagamento() {
        Pedidos pedido = new Pedidos();
        pedido.setStatus(StatusPedido.PENDENTE);
        PedidoDoVendedor item = PedidoDoVendedor.builder().status(StatusPedido.PENDENTE).build();
        pedido.setItens(List.of(item));

        pedido.confirmarPagamento();

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.APROVADO);
        assertThat(item.getStatus()).isEqualTo(StatusPedido.APROVADO);
    }

    @Test
    @DisplayName("Deve criar pedido a partir de itens do carrinho com cálculos corretos")
    void deveCriarPedidoDeItensCarrinho() {
        UUID compradorId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();
        UUID vendedorId = UUID.randomUUID();
        CarrinhoItem itemCarrinho = new CarrinhoItem( produtoId, "nao", new BigDecimal(11.2),  2L);
        Produtos produto = new Produtos();
        produto.setId(produtoId);
        produto.setNomeProduto("Produto Teste");
        produto.setPreco(BigDecimal.TEN);
        produto.setVendedorId(vendedorId);

        Map<UUID, Produtos> produtos = Map.of(produtoId, produto);
        Map<UUID, String> stripeAccounts = Map.of(vendedorId, "acct_123");

        Pedidos pedido = Pedidos.criarDe(compradorId, List.of(itemCarrinho), produtos, stripeAccounts, "Rua Teste");

        assertThat(pedido.getCompradorId()).isEqualTo(compradorId);
        assertThat(pedido.getEndereco()).isEqualTo("Rua Teste");
        assertThat(pedido.getPreco()).isEqualByComparingTo(BigDecimal.valueOf(20));
        assertThat(pedido.getItens()).hasSize(1);
        
        PedidoDoVendedor itemPedido = pedido.getItens().get(0);
        assertThat(itemPedido.getProdutoId()).isEqualTo(produtoId);
        assertThat(itemPedido.getQuantidade()).isEqualTo(2L);
        assertThat(itemPedido.getValor()).isEqualByComparingTo(BigDecimal.valueOf(20));
        assertThat(itemPedido.getStripeAccountId()).isEqualTo("acct_123");
    }
}
