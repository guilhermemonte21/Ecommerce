package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.EstoqueInsuficienteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProdutosTest {

    @Test
    @DisplayName("Deve atualizar estoque corretamente")
    void deveAtualizarEstoqueCorretamente() {
        Produtos produto = new Produtos();
        produto.setEstoque(10L);

        produto.atualizarEstoque(5L);

        assertEquals(15L, produto.getEstoque());
    }

    @Test
    @DisplayName("Deve lidar com estoque inicial nulo ao atualizar")
    void deveLidarComEstoqueNuloAoAtualizar() {
        Produtos produto = new Produtos();
        produto.setEstoque(null);

        produto.atualizarEstoque(5L);

        assertEquals(5L, produto.getEstoque());
    }

    @Test
    @DisplayName("Deve reservar estoque com sucesso")
    void deveReservarEstoqueComSucesso() {
        Produtos produto = new Produtos();
        produto.setNomeProduto("Produto Teste");
        produto.setEstoque(10L);

        produto.reservarEstoque(4L);

        assertEquals(6L, produto.getEstoque());
    }

    @Test
    @DisplayName("Deve lançar exceção ao reservar mais estoque do que o disponível")
    void deveLancarExcecaoAoReservarEstoqueInsuficiente() {
        Produtos produto = new Produtos();
        produto.setNomeProduto("Produto Teste");
        produto.setEstoque(5L);

        assertThrows(EstoqueInsuficienteException.class, () -> {
            produto.reservarEstoque(6L);
        });
    }

    @Test
    @DisplayName("Deve identificar corretamente se o produto pertence ao vendedor")
    void deveValidarSeProdutoPertenceAoVendedor() {
        UUID vendedorId = UUID.randomUUID();
        Produtos produto = new Produtos();
        produto.setVendedorId(vendedorId);

        assertTrue(produto.pertenceA(vendedorId));
        assertFalse(produto.pertenceA(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve aplicar atualização de dados corretamente")
    void deveAplicarAtualizacaoCorretamente() {
        Produtos produto = new Produtos();
        produto.setNomeProduto("Antigo");
        produto.setDescricao("Antiga");
        produto.setPreco(BigDecimal.TEN);
        produto.setEstoque(1L);

        produto.aplicarAtualizacao("Novo", "Nova", BigDecimal.valueOf(20), 10L);

        assertEquals("Novo", produto.getNomeProduto());
        assertEquals("Nova", produto.getDescricao());
        assertEquals(BigDecimal.valueOf(20), produto.getPreco());
        assertEquals(10L, produto.getEstoque());
    }
}
