package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.CarrinhoItem;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Mappers.PedidoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.CriarPedido.CriarPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.CarrinhoVazioException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.EstoqueInsuficienteException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PedidoCriadoEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarPedidoTest {

    @Mock private PedidoGateway pedidoGateway;
    @Mock
    private CarrinhoGateway carrinhoGateway;
    @Mock private ProdutoGateway produtoGateway;
    @Mock private PedidoMapperApl mapperApl;
    @Mock private UsuarioAutenticadoGateway authGateway;
    @Mock private EventPublisher eventPublisher;
    @Mock private UsuarioGateway usuarioGateway;

    @InjectMocks
    private CriarPedido criarPedido;

    private UUID userId;
    private UUID produtoId;
    private UUID vendedorId;
    private UsuarioAutenticado userAutenticado;
    private Carrinho carrinho;
    private Produtos produto;

    @BeforeEach
    void setup() {
        userId    = UUID.randomUUID();
        produtoId = UUID.randomUUID();
        vendedorId = UUID.randomUUID();

        Usuarios usuario = new Usuarios();
        usuario.setId(userId);

        userAutenticado = new UsuarioAutenticado(usuario);


        CarrinhoItem item = new CarrinhoItem();
        item.setProdutoId(produtoId);
        item.setQuantidade(2L);

        carrinho = new Carrinho();
        carrinho.setItens(List.of(item));

        produto = new Produtos();
        produto.setId(produtoId);
        produto.setVendedorId(vendedorId);
        produto.setNomeProduto("Produto Teste");
        produto.setPreco(new BigDecimal("50.00"));
        produto.setEstoque(10L);

        lenient().when(authGateway.get()).thenReturn(userAutenticado);
        lenient().when(carrinhoGateway.getByDono(userId)).thenReturn(carrinho);
        lenient().when(produtoGateway.getByIdComLock(produtoId)).thenReturn(Optional.of(produto));
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso")
    void deveCriarPedidoComSucesso() {
        Usuarios vendedor = new Usuarios();
        vendedor.setId(vendedorId);
        vendedor.setStripeAccountId("acct_123");

        Pedidos pedidoSalvo = new Pedidos();
        pedidoSalvo.setId(UUID.randomUUID());



        when(usuarioGateway.findAllByIds(List.of(vendedorId))).thenReturn(List.of(vendedor));
        when(pedidoGateway.save(any())).thenReturn(pedidoSalvo);
        when(mapperApl.toResponse(any())).thenReturn(new PedidoResponse(pedidoSalvo.getId(), pedidoSalvo.getCompradorId(), null, pedidoSalvo.getEndereco(), pedidoSalvo.getPreco(), "Pendente", pedidoSalvo.getCriadoEm() ));

        PedidoResponse result = criarPedido.criarPedido("Rua Teste, 123");

        assertNotNull(result);
        verify(produtoGateway).saveAll(anyList());
        verify(pedidoGateway).save(any());
        verify(eventPublisher).publish(any(PedidoCriadoEvent.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando endereço for nulo")
    void deveLancarExcecaoQuandoEnderecoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> criarPedido.criarPedido(null));
    }

    @Test
    @DisplayName("Deve lançar exceção quando endereço for vazio")
    void deveLancarExcecaoQuandoEnderecoVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> criarPedido.criarPedido("   "));
    }

    @Test
    @DisplayName("Deve lançar exceção quando carrinho estiver vazio")
    void deveLancarExcecaoQuandoCarrinhoVazio() {
        carrinho.setItens(Collections.emptyList());

        assertThrows(CarrinhoVazioException.class,
                () -> criarPedido.criarPedido("Rua Teste, 123"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando carrinho for nulo")
    void deveLancarExcecaoQuandoCarrinhoNulo() {
        when(carrinhoGateway.getByDono(userId)).thenReturn(null);

        assertThrows(CarrinhoVazioException.class,
                () -> criarPedido.criarPedido("Rua Teste, 123"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando estoque insuficiente")
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        produto.setEstoque(1L); // carrinho pede 2

        assertThrows(EstoqueInsuficienteException.class,
                () -> criarPedido.criarPedido("Rua Teste, 123"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando vendedor tenta comprar próprio produto")
    void deveLancarExcecaoQuandoVendedorCompraProprioProduto() {
        produto.setVendedorId(userId); // vendedor == comprador

        assertThrows(IllegalArgumentException.class,
                () -> criarPedido.criarPedido("Rua Teste, 123"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não encontrado")
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        when(produtoGateway.getByIdComLock(produtoId)).thenReturn(Optional.empty());

        assertThrows(ProdutoNotFoundException.class,
                () -> criarPedido.criarPedido("Rua Teste, 123"));
    }

    @Test
    @DisplayName("Deve descontar estoque corretamente")
    void deveDescontarEstoqueCorretamente() {
        Usuarios vendedor = new Usuarios();
        vendedor.setId(vendedorId);
        vendedor.setStripeAccountId("acct_123");

        Pedidos pedidoSalvo = new Pedidos();
        pedidoSalvo.setId(UUID.randomUUID());

        when(usuarioGateway.findAllByIds(any())).thenReturn(List.of(vendedor));
        when(pedidoGateway.save(any())).thenReturn(pedidoSalvo);
        when(mapperApl.toResponse(any())).thenReturn(null);

        criarPedido.criarPedido("Rua Teste, 123");

        // estoque era 10, pediu 2 → esperado 8
        assertEquals(8L, produto.getEstoque());
    }
}