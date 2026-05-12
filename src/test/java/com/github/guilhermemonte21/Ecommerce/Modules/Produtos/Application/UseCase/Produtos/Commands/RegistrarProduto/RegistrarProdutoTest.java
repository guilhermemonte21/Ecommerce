package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Commands.RegistrarProduto;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoCommandGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Service.ProdutoAuthorizationService;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.ProdutoAlteradoEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarProdutoTest {

    @Mock
    private ProdutoCommandGateway gateway;
    @Mock
    private ProdutoMapperApl produtoMapper;
    @Mock
    private ProdutoAuthorizationService authorizationService;
    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private RegistrarProduto registrarProduto;

    private Usuarios usuario;
    private UsuarioAutenticado usuarioAutenticado;

    @BeforeEach
    void setUp() {
        usuario = new Usuarios();
        usuario.setId(UUID.randomUUID());
        usuario.setNome("Vendedor Teste");
        usuarioAutenticado = new UsuarioAutenticado(usuario);
    }

    @Test
    @DisplayName("Deve registrar um produto com sucesso")
    void deveRegistrarProdutoComSucesso() {
        // Given
        CreateProdutoRequest request = new CreateProdutoRequest("Produto Teste", "Descrição Teste", BigDecimal.TEN, 10L);
        Produtos produtoDomain = new Produtos(UUID.randomUUID(), "Produto Teste", "Descrição Teste", BigDecimal.TEN, 10L);
        produtoDomain.setVendedorId(usuario.getId());
        
        when(authorizationService.validarVendedorAtivo()).thenReturn(usuarioAutenticado);
        when(produtoMapper.toDomain(eq(request), eq(usuario.getId()))).thenReturn(produtoDomain);
        when(gateway.salvar(any(Produtos.class))).thenReturn(produtoDomain);
        when(produtoMapper.toResponse(any(Produtos.class), anyString())).thenReturn(new ProdutoResponse(produtoDomain.getId(), "Produto Teste", "Vendedor Teste", BigDecimal.TEN, "Descrição Teste", 10L));

        // When
        ProdutoResponse response = registrarProduto.create(request);

        // Then
        assertNotNull(response);
        assertEquals("Produto Teste", response.NomeProduto());
        verify(gateway).salvar(produtoDomain);
        verify(eventPublisher).publish(any(ProdutoAlteradoEvent.class));
        verify(authorizationService).validarVendedorAtivo();
    }
}
