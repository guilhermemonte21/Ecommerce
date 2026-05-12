package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.DeletarProduto;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoCommandGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Service.ProdutoAuthorizationService;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.ProdutoAlteradoEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarProdutoTest {

    @Mock
    private ProdutoCommandGateway gateway;
    @Mock
    private ProdutoAuthorizationService authorizationService;
    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private DeletarProduto deletarProduto;

    @Test
    @DisplayName("Deve deletar um produto com sucesso")
    void deveDeletarProdutoComSucesso() {
        // Given
        UUID produtoId = UUID.randomUUID();
        UUID vendedorId = UUID.randomUUID();
        Produtos produto = new Produtos();
        produto.setId(produtoId);
        produto.setVendedorId(vendedorId);

        when(gateway.getById(produtoId)).thenReturn(Optional.of(produto));

        // When
        deletarProduto.deletar(produtoId);

        // Then
        verify(gateway).delete(produto);
        verify(authorizationService).validarProprietario(vendedorId);
        verify(eventPublisher).publish(any(ProdutoAlteradoEvent.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o produto não for encontrado")
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        // Given
        UUID produtoId = UUID.randomUUID();
        when(gateway.getById(produtoId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProdutoNotFoundException.class, () -> deletarProduto.deletar(produtoId));
        verify(gateway, never()).delete(any());
        verify(eventPublisher, never()).publish(any());
    }
}
