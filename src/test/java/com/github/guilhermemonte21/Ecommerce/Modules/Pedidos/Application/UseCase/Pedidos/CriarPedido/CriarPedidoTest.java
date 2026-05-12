package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.CriarPedido;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.CarrinhoItem;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.CriarPedidoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Mappers.PedidoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.CarrinhoVazioException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNaoPertenceAoVendedorException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PedidoCriadoEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarPedidoTest {

    @Mock
    private PedidoGateway pedidoGateway;
    @Mock
    private CarrinhoGateway carrinhoGateway;
    @Mock
    private ProdutoGateway produtoGateway;
    @Mock
    private PedidoMapperApl mapperApl;
    @Mock
    private UsuarioAutenticadoGateway authGateway;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private UsuarioGateway usuarioGateway;

    @InjectMocks
    private CriarPedido criarPedido;

    @Test
    @DisplayName("Deve criar um pedido com sucesso")
    void deveCriarPedidoComSucesso() {
        // Arrange
        UUID compradorId = UUID.randomUUID();
        UUID vendedorId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();
        CriarPedidoRequest request = new CriarPedidoRequest("Rua Teste, 123");

        Usuarios comprador = new Usuarios();
        comprador.setId(compradorId);
        comprador.setNome("Comprador");
        comprador.setEmail("comprador@teste.com");

        UsuarioAutenticado userAuth = mock(UsuarioAutenticado.class);
        when(userAuth.getUser()).thenReturn(comprador);
        when(authGateway.get()).thenReturn(userAuth);

        Carrinho carrinho = new Carrinho();
        carrinho.setItens(List.of(new CarrinhoItem(produtoId, "Produto Teste", BigDecimal.TEN, 1L)));
        when(carrinhoGateway.getByDono(compradorId)).thenReturn(carrinho);

        Produtos produto = new Produtos();
        produto.setId(produtoId);
        produto.setVendedorId(vendedorId);
        produto.setNomeProduto("Produto Teste");
        produto.setPreco(BigDecimal.TEN);
        produto.setEstoque(10L);
        when(produtoGateway.getByIdComLock(produtoId)).thenReturn(Optional.of(produto));

        Usuarios vendedor = new Usuarios();
        vendedor.setId(vendedorId);
        vendedor.setStripeAccountId("acct_vendedor");
        when(usuarioGateway.findAllByIds(any())).thenReturn(List.of(vendedor));
        when(usuarioGateway.getById(compradorId)).thenReturn(Optional.of(comprador));

        when(pedidoGateway.save(any(Pedidos.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapperApl.toResponse(any(Pedidos.class))).thenReturn(mock(PedidoResponse.class));

        // Act
        PedidoResponse response = criarPedido.criarPedido(request);

        // Assert
        assertThat(response).isNotNull();
        verify(pedidoGateway).save(any(Pedidos.class));
        verify(eventPublisher).publish(any(PedidoCriadoEvent.class));
        verify(produtoGateway).saveAll(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar pedido com carrinho vazio")
    void deveLancarExcecaoCarrinhoVazio() {
        // Arrange
        UUID compradorId = UUID.randomUUID();
        CriarPedidoRequest request = new CriarPedidoRequest("Endereço");

        Usuarios comprador = new Usuarios();
        comprador.setId(compradorId);
        UsuarioAutenticado userAuth = mock(UsuarioAutenticado.class);
        when(userAuth.getUser()).thenReturn(comprador);
        when(authGateway.get()).thenReturn(userAuth);

        Carrinho carrinho = new Carrinho(); // vazio
        when(carrinhoGateway.getByDono(compradorId)).thenReturn(carrinho);

        // Act & Assert
        assertThatThrownBy(() -> criarPedido.criarPedido(request))
                .isInstanceOf(CarrinhoVazioException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar comprar o próprio produto")
    void deveLancarExcecaoComprarProprioProduto() {
        // Arrange
        UUID compradorId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();
        CriarPedidoRequest request = new CriarPedidoRequest("Endereço");

        Usuarios comprador = new Usuarios();
        comprador.setId(compradorId);
        UsuarioAutenticado userAuth = mock(UsuarioAutenticado.class);
        when(userAuth.getUser()).thenReturn(comprador);
        when(authGateway.get()).thenReturn(userAuth);

        Carrinho carrinho = new Carrinho();
        carrinho.setItens(List.of(new CarrinhoItem(produtoId, "Monitor", new BigDecimal(11), 1L)));
        when(carrinhoGateway.getByDono(compradorId)).thenReturn(carrinho);

        Produtos produto = new Produtos();
        produto.setId(produtoId);
        produto.setVendedorId(compradorId); // Mesmo ID do comprador
        produto.setNomeProduto("Produto Meu");
        when(produtoGateway.getByIdComLock(produtoId)).thenReturn(Optional.of(produto));

        // Act & Assert
        assertThatThrownBy(() -> criarPedido.criarPedido(request))
                .isInstanceOf(ProdutoNaoPertenceAoVendedorException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o produto não é encontrado")
    void deveLancarExcecaoProdutoNaoEncontrado() {
        // Arrange
        UUID compradorId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();
        CriarPedidoRequest request = new CriarPedidoRequest("Endereço");

        Usuarios comprador = new Usuarios();
        comprador.setId(compradorId);
        UsuarioAutenticado userAuth = mock(UsuarioAutenticado.class);
        when(userAuth.getUser()).thenReturn(comprador);
        when(authGateway.get()).thenReturn(userAuth);

        Carrinho carrinho = new Carrinho();
        carrinho.setItens(List.of(new CarrinhoItem(produtoId, "Monitor", new BigDecimal(11), 1L)));
        when(carrinhoGateway.getByDono(compradorId)).thenReturn(carrinho);

        when(produtoGateway.getByIdComLock(produtoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> criarPedido.criarPedido(request))
                .isInstanceOf(com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o estoque é insuficiente")
    void deveLancarExcecaoEstoqueInsuficiente() {
        // Arrange
        UUID compradorId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();
        CriarPedidoRequest request = new CriarPedidoRequest("Endereço");

        Usuarios comprador = new Usuarios();
        comprador.setId(compradorId);
        UsuarioAutenticado userAuth = mock(UsuarioAutenticado.class);
        when(userAuth.getUser()).thenReturn(comprador);
        when(authGateway.get()).thenReturn(userAuth);

        Carrinho carrinho = new Carrinho();
        carrinho.setItens(List.of(new CarrinhoItem(produtoId, "Monitor", new BigDecimal(11), 10L)));
        when(carrinhoGateway.getByDono(compradorId)).thenReturn(carrinho);

        Produtos produto = new Produtos();
        produto.setId(produtoId);
        produto.setVendedorId(UUID.randomUUID());
        produto.setNomeProduto("Monitor");
        produto.setEstoque(5L); // Menos que o solicitado (10)
        when(produtoGateway.getByIdComLock(produtoId)).thenReturn(Optional.of(produto));

        // Act & Assert
        assertThatThrownBy(() -> criarPedido.criarPedido(request))
                .isInstanceOf(com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.EstoqueInsuficienteException.class);
    }
}
